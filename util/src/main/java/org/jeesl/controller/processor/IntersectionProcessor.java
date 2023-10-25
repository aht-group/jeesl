package org.jeesl.controller.processor;

import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.collections.ListUtils;

import net.sf.ahtutils.controller.processor.set.SetProcessingBaseVisitor;
import net.sf.ahtutils.controller.processor.set.SetProcessingLexer;
import net.sf.ahtutils.controller.processor.set.SetProcessingParser;

public class IntersectionProcessor extends SetProcessingBaseVisitor
{
	@SuppressWarnings("unchecked")
	public static <T> List<T> and(List<T> a, List<T> b)
	{
		return ListUtils.intersection(a,b);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> or(List<T> a, List<T> b)
	{
		return ListUtils.union(ListUtils.subtract(a,b),b);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> subtract(List<T> a, List<T> b)
	{
		return ListUtils.subtract(a,b);
	}

	static List lists;

	@SuppressWarnings("unchecked")
	public static <T> List<T> query(String query, List<List<T>> l)
	{
		lists = l;
		SetProcessingLexer lexer = new SetProcessingLexer(CharStreams.fromString(query));
		SetProcessingParser parser = new SetProcessingParser(new CommonTokenStream(lexer));
		return (List<T>)new IntersectionProcessor().visit(parser.parse());
	}

	@Override
	public Object visitExpression(SetProcessingParser.ExpressionContext ctx)
	{
		if(ctx.op == null) {
			return asList(ctx.left.getText().charAt(0));
		}
		if(ctx.op.AND() != null) {
			return and(asList(ctx.left.getText().charAt(0)),(List)visitExpression(ctx.expression()));
		}
		if(ctx.op.OR() != null) {
			return or(asList(ctx.left.getText().charAt(0)),(List)visitExpression(ctx.expression()));
		}
		throw new RuntimeException("*JediHandWave* This is never happened.");
	}

	@Override
	public Object visitParse(SetProcessingParser.ParseContext ctx) {return super.visitParse(ctx);}

	private List asList(char s) {
		int i = s - 97;
		return (List)lists.get(i);
	}
}