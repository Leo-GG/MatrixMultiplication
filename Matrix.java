/**
* @file Matrix.java
* @brief Contiene la clase Matrix
*
* Representa una matriz de dos dimensiones y tiene metodos que permiten
* acceder tanto a las dimensiones como a los valores de la matriz.
*/

import java.util.Arrays;

/**
* @class Matrix
* Matriz bidimensional
*/
public class Matrix {

	private float[][] values_;
	private int rows_;
	private int cols_;

	/**
        * Constructor
        * @param inputValues Valores de la matriz
	* @param rows Número de filas
	* @param cols Número de columnas
        */
	public Matrix(float[][] inputValues, int rows, int cols){          
	 	  		 	                                
		rows_=rows;
		cols_=cols;
		values_= new float[rows_][cols_];
		for (int i=0;i<rows_;i++){
			for (int j=0;j<cols_;j++){
				values_[i][j]=inputValues[i][j];
			}
		}
	}
	
	/**
        * Devuelve el número de filas de la matriz
	* @return M Número de filas
        */
	public int getM(){
		return rows_;
	}

	/**
        * Devuelve el número de columnas de la matriz
	* @return N Número de columnas
        */
	public int getN(){
		return cols_;
	}

	/**
        * Devuelve todos los valores de la matriz
	* @return values Valores de la matriz
        */
	public float[][] getValues(){
		return values_;
	}
}
