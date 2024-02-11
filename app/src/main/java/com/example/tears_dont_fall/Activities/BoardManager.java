package com.example.tears_dont_fall.Activities;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tears_dont_fall.R;

import java.util.Arrays;
import java.util.Random;

public class BoardManager {
    private GameObject[] matrix;
    private int numRows;
    private int numCols;
    private Random random = new Random();
    private int objCount = 0;

    View[][] ivs;

    public BoardManager(int numRows, int numCols, LinearLayout[] ll) {
        this.numRows = numRows;
        this.numCols = numCols;
        matrix = new GameObject[numRows];
        ivs = new View[numRows][numCols];

        for (int i = 0; i < ll.length; i++) {
            for (int j =0; j < ll[i].getChildCount(); j++){
                ivs[i][j] = ll[i].getChildAt(j);
                ivs[i][j].setBackgroundResource(R.drawable.parachute);
                ivs[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void turnOffRow(int row){
        for (View iv:
        ivs[row]) {
            iv.setVisibility(View.INVISIBLE);
        }
    }

    public void moveDown() {
        // Shift each row down
        turnOffRow(numRows-1);
        for (int i = numRows - 1; i > 0; i--) {
            matrix[i] = matrix[i - 1];
            turnOffRow(i-1);
            if (matrix[i] != null)
                matrix[i].switchViews(ivs[i]);
        }
        // Clear the first row
        matrix[0] = null;
    }

    public void add(GameObject gameObject) {
        int column = random.nextInt(numCols); // Generate a random column index
        matrix[0] = gameObject;
        matrix[0].switchViews((View[]) ivs[0]);
        matrix[0].setPos(column);
    }

    public boolean check(int column) {
        if (column < 0 || column >= numCols) {
            throw new IllegalArgumentException("Invalid column index");
        }
        return matrix[numRows - 1] != null && matrix[numRows - 1].getPos() == column;
    }

    public int getObjCount() {
        return objCount;
    }

    public void addObjCount() {
        this.objCount++;
    }
}
