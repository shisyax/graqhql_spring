package com.example.demo.graphql.hello;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.Arrays;

/**
 * FibonacciFetcher is a GraphQL data fetcher that computes Fibonacci numbers.
 * It uses memoization to efficiently compute and store Fibonacci numbers.
 * The `fibonacci` query takes two arguments: `n` (the number of Fibonacci numbers to return)
 * and `start` (the starting index from which to return the Fibonacci sequence).
 * Query example:
 * ```
 * query {
 * *   fib(n: 5, start: 1)
 * * }
 * ```
 * * This will return the first 5 Fibonacci numbers starting from index 1.
 * ```
 * query {
 *    fib(n: 5, start: 2)
 * }
 * * This will return the 5 Fibonacci numbers starting from index 2.
 */
@DgsComponent
public class FibonacciFetcher {
    // Memoization array to store Fibonacci numbers
    private static int[] memoArray = new int[0];

    @SuppressWarnings("unused")
    @DgsQuery(field = "fib")
    public int[] fibonacci(@InputArgument int n,@InputArgument int start) {
        if (n < 0 ) {
            throw new IllegalArgumentException("Input must be a non-negative integer.");
        }
        if(start < 1){
            throw new IllegalArgumentException("Start must be a positive integer.");
        }
        updateMemoArray(n,start);
        return Arrays.copyOfRange(memoArray, start, start+n);
    }

    private static void updateMemoArray(int n,int start){
        if(memoArray.length == 0){
            // Initialize the memoization array
            int[] newMemoArray = Arrays.copyOf(memoArray, n + 1);
            newMemoArray[0] = 0;
            newMemoArray[1] = 1;
            for (int i = 2; i <= n; i++) {
                newMemoArray[i] = newMemoArray[i - 1] + newMemoArray[i - 2];
            }
            memoArray = newMemoArray;
        }
        else if (memoArray.length < start + n + 1) {
            int[] newMemoArray = Arrays.copyOf(memoArray, n + 1);
            // fill in the missing values
            for (int i = memoArray.length; i <= n; i++) {
                newMemoArray[i] = newMemoArray[i - 1] + newMemoArray[i - 2];
            }
            memoArray = newMemoArray;
        }
    }
}
