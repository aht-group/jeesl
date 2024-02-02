package org.jeesl.factory.txt.io.ssi.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Combinations;

public class TxtIoSsiSimilarityFactory
{

	
	public static List<String> permutations(String input)
	{
		List<String> result = new ArrayList<>();
		
		String[] array = input.split(" ");
		
		Combinations combinations = new Combinations(array.length,array.length);
        for (int[] combinationIndices : combinations)
        {
            permuteAndPrint(array, combinationIndices, result);
        }
		
		return result;
	}
	
	private static void permuteAndPrint(String[] array, int[] combinationIndices, List<String> result)
	{
        int r = combinationIndices.length;
        String[] permutation = new String[r];

        for (int i = 0; i < r; i++)
        {
            permutation[i] = array[combinationIndices[i]];
        }

        permute(array, permutation, 0, r-1, result);
    }
	
    private static void permute(String[] array, String[] permutation, int left, int right, List<String> result)
    {
        if (left == right)
        {
        	result.add(String.join(" ", permutation));
//            System.out.println(Arrays.toString(permutation));
        }
        else
        {
            for (int i = left; i <= right; i++)
            {
                swap(permutation, left, i);
                permute(array, permutation, left + 1, right, result);
                swap(permutation, left, i); // Zurücksetzen für die nächste Iteration
            }
        }
    }
    
    private static void swap(String[] array, int i, int j)
    {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}