package P6;

public class P {
    // DP solution to calculate the maximum sum in a given array
    // with no adjacent elements considered (function uses an extra space)
    public static int findMaxSumSubsequence(int[] nums)
    {
        int n = nums.length;

        // base case
        if (n == 0) {
            return 0;
        }

        // base case
        if (n == 1) {
            return nums[0];
        }

        // create an auxiliary array to store solutions to subproblems
        int[] lookup = new int[n];

        // lookup[i] stores the maximum sum possible till index `i`

        // trivial case
        lookup[0] = nums[0];
        lookup[1] = Integer.max(nums[0], nums[1]);

        // traverse array from index 2
        for (int i = 2; i < n; i++)
        {
            // lookup[i] stores the maximum sum we get by

            // 1. Excluding the current element and take maximum sum until index `i-1`
            // 2. Including the current element nums[i] and take the maximum sum
            //	till until i-2
            lookup[i] = Integer.max(lookup[i - 1], lookup[i - 2] + nums[i]);

            // if the current element is more than the maximum sum until the current
            // element
            lookup[i] = Integer.max(lookup[i], nums[i]);
        }

        // return maximum sum
        return lookup[n - 1];
    }

    public static void main(String[] args)
    {
        int[] nums = {9,5,3,8,5,1,7,4,3,5};
        System.out.print("The maximum sum is " + findMaxSumSubsequence(nums));
    }
}
