package org.jeesl.controller.processor;

import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.collections.ListUtils;

import net.sf.ahtutils.controller.processor.bool.BoolProcessingBaseVisitor;
import net.sf.ahtutils.controller.processor.bool.BoolProcessingLexer;
import net.sf.ahtutils.controller.processor.bool.BoolProcessingParser;

public class BooleanProcessor extends BoolProcessingBaseVisitor
{
	public static Boolean and(Boolean a, Boolean b)
	{
		return a && b;
	}

	public static Boolean or(Boolean a, Boolean b)
	{
		return a || b;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Boolean> subtract(List<Boolean> a, List<Boolean> b)
	{
		return ListUtils.subtract(a,b);
	}

	static List<Boolean> booleanList;


	public static Boolean query(String query, List<Boolean> l)
	{
		booleanList = l;
		BoolProcessingLexer lexer = new BoolProcessingLexer(CharStreams.fromString(query));
		BoolProcessingParser parser = new BoolProcessingParser(new CommonTokenStream(lexer));
		return (Boolean)new BooleanProcessor().visit(parser.parse());
	}

	@Override
	public Object visitExpression(BoolProcessingParser.ExpressionContext ctx)
	{
		if(ctx.op == null) {
			return booleanList.get(0);
		}
		if(ctx.op.AND() != null) {
			boolean tmp = and(booleanList.get(0),booleanList.get(1));
			for(int i = 2; i < booleanList.size(); i++)
			{
				tmp =  and(tmp, booleanList.get(i));
			}
			return tmp;
		}
		if(ctx.op.OR() != null) {
			boolean tmp = or(booleanList.get(0),booleanList.get(1));
			for(int i = 2; i < booleanList.size(); i++)
			{
				tmp =  or(tmp, booleanList.get(i));
			}
			return tmp;
		}
		throw new RuntimeException("*JediHandWave* This is never happened.");
	}

	@Override
	public Object visitParse(BoolProcessingParser.ParseContext ctx) {return super.visitParse(ctx);}

	private Boolean asBoolean(String s) {
		return s.equals("true");
	}
}