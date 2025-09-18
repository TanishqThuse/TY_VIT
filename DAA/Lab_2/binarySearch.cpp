#include <iostream>
#include <vector>
using namespace std;

// Recursive Binary Search function
int binarySearchRecursive(vector<int>& arr, int left, int right, int target) {
    if (left > right) {
        return -1; // Target not found
    }

    int mid = left + (right - left) / 2;

    if (arr[mid] == target) {
        return mid; // Target found
    }
    else if (arr[mid] > target) {
        return binarySearchRecursive(arr, left, mid - 1, target); // Search in the left half
    }
    else {
        return binarySearchRecursive(arr, mid + 1, right, target); // Search in the right half
    }
}

// Iterative Binary Search function
int binarySearchIterative(vector<int>& arr, int target) {
    int left = 0, right = arr.size() - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return mid; // Target found
        }
        else if (arr[mid] > target) {
            right = mid - 1; // Search in the left half
        }
        else {
            left = mid + 1; // Search in the right half
        }
    }

    return -1; // Target not found
}

int main() {
    int n, target;

    // Take user input for array size
    cout << "Enter the number of elements in the array: ";
    cin >> n;

    vector<int> arr(n);

    // Take user input for array elements
    cout << "Enter the elements of the array (in sorted order): ";
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }

    // Take user input for target element
    cout << "Enter the target element to search for: ";
    cin >> target;

    // Test recursive binary search
    int resultRecursive = binarySearchRecursive(arr, 0, n - 1, target);
    if (resultRecursive != -1) {
        cout << "Recursive: Element found at index " << resultRecursive << endl;
    } else {
        cout << "Recursive: Element not found!" << endl;
    }

    // Test iterative binary search
    int resultIterative = binarySearchIterative(arr, target);
    if (resultIterative != -1) {
        cout << "Iterative: Element found at index " << resultIterative << endl;
    } else {
        cout << "Iterative: Element not found!" << endl;
    }

    return 0;
}
