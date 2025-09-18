/**Assignment Based on Divide and Conquer Strategy. (Implement Recursive and Non-Recursive Binary Search Algorithm using C++ or java. Determine Time and space complexity) */

import java.util.Scanner;

public class BinarySearchExample {

    public static int recursiveBinarySearch(int[] arr, int low, int high, int x) {

        if (high >= low) {

            int mid = low + (high - low) / 2;

            if (arr[mid] == x)
                return mid;

            if (arr[mid] > x)
                return recursiveBinarySearch(arr, low, mid - 1, x);

            return recursiveBinarySearch(arr, mid + 1, high, x);

        }

        return -1;

    }

    public static int iterativeBinarySearch(int[] arr, int x) {

        int low = 0, high = arr.length - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            if (arr[mid] == x)

                return mid;

            if (arr[mid] < x)

                low = mid + 1;

            else

                high = mid - 1;

        }

        return -1;

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[] arr = new int[n];

        for (int i = 0; i < n; i++)

            arr[i] = sc.nextInt();

        int key = sc.nextInt();

        int resRec = recursiveBinarySearch(arr, 0, n - 1, key);

        System.out.println(resRec);

        int resIter = iterativeBinarySearch(arr, key);

        System.out.println(resIter);

        sc.close();

    }

}