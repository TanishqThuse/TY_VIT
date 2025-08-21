// Time complexity : O(n)

// #include<bits/stdc++.h>
// #include<iostream.h>
#include<iostream>
#include<vector>
using namespace std;

void printVector(vector<int>& vec){
    for(int ele : vec){
        cout<<ele<<" ";
    }
    cout<<" ";
}

void takeInput(vector<int>& vec, int n){
    for(int i=0; i<n; i++){
        int x;
        cin>>x;
        vec.push_back(x);
    }
}

int main(){
    int n;
    // vector<int> vec;
    vector<int> vec;    
    cin>>n;

    takeInput(vec,n);

    // core logic of bubble sort
    for(int i=0; i<n; i++){
        for(int j=0; j<n-1-i; j++){
            if(vec[j] > vec[j+1]){
                // swap both of them
                vec[j] = vec[j] + vec[j+1];
                vec[j+1] = vec[j] - vec[j+1];
                vec[j] = vec[j] - vec[j+1];
            }
        }
    }

    // print result
    printVector(vec);
}
