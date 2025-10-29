#include <iostream>
using namespace std;

int main() {
    int n, W;
    cout << "Enter number of items: ";
    cin >> n;

    int value[20], weight[20];
    cout << "Enter values of items:\n";
    for (int i = 0; i < n; i++)
        cin >> value[i];

    cout << "Enter weights of items:\n";
    for (int i = 0; i < n; i++)
        cin >> weight[i];

    cout << "Enter capacity of knapsack: ";
    cin >> W;

    int dp[20][50]; 

    for (int i = 0; i <= n; i++) {
        for (int w = 0; w <= W; w++) {
            if (i == 0 || w == 0)
                dp[i][w] = 0;
            else if (weight[i - 1] <= w)
                dp[i][w] = max(value[i - 1] + dp[i - 1][w - weight[i - 1]], dp[i - 1][w]);
            else
                dp[i][w] = dp[i - 1][w];
        }
    }

    cout << "\nMaximum value in knapsack = " << dp[n][W] << endl;

    return 0;
}

/**
 * 
PS D:\Backup\Tanishq\VIT\VIT_File\TY_VIT\DAA\Lab_4_Knapsack> g++ .\knapsack.cpp -o main        
PS D:\Backup\Tanishq\VIT\VIT_File\TY_VIT\DAA\Lab_4_Knapsack> ./main
Enter number of items: 4
Enter values of items:
30 60 90 100
Enter weights of items:
30 20 50 40
Enter capacity of knapsack: 60

Maximum value in knapsack = 160
PS D:\Backup\Tanishq\VIT\VIT_File\TY_VIT\DAA\Lab_4_Knapsack> 

 */
