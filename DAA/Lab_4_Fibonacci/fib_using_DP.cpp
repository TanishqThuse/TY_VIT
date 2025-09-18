#include <iostream>
#include <vector>
using namespace std;

// Fibonacci using Dynamic Programming (Bottom-up approach)
long long fibonacciDP(int n) {
    if (n <= 1) return n;
    
    vector<long long> dp(n + 1);
    dp[0] = 0;
    dp[1] = 1;
    
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i-1] + dp[i-2];
    }
    
    return dp[n];
}

// Space optimized version
long long fibonacciOptimized(int n) {
    if (n <= 1) return n;
    
    long long prev2 = 0, prev1 = 1, current;
    
    for (int i = 2; i <= n; i++) {
        current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }
    
    return current;
}

int main() {
    int n;
    cout << "Enter the position of Fibonacci number: ";
    cin >> n;
    
    cout << "Fibonacci(" << n << ") using DP: " << fibonacciDP(n) << endl;
    cout << "Fibonacci(" << n << ") optimized: " << fibonacciOptimized(n) << endl;
    
    return 0;
}