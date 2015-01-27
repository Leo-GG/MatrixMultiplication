/**
* @mainpage Multiplicación Asociativa de Matrices
*
* <b>Función del programa</b>: <p>Lee una lista de ordenada de matrices 
* y encuentra la asociación que minimiza el número de operaciones requeridas
* para multiplicarlas</p>
*
* <b>Datos de entrada</b>: <p>El fichero de datos de entrada consta de una
* linea que indica el numero de matrices seguida de una descripción de cada
* matriz. Esta descripción consiste en una linea en la que se indican las 
* dimensiones de la matriz separadas por un espacio y a continuación tantas
* lineas como filas tenga la matriz. En cada fila se dan los valores 
* correspondientes a cada componente de la matriz separados por un espacio</p>
*
* <b>Datos de salida</b>: <p>El programa imprime una linea con la asociación
* óptima, una línea que indica el número de operaciones necesarias para 
* realizar la multiplicación y la matriz resultado en el mismo formato que las
* matrices de entrada</p>
*
* <b>Uso</b>: <p>$java multimat [-t][-h] [fichero_entrada] [fichero_salida]
* </p>
*              
* @author Leonardo Garma
* @version 0.1.0 09/01/2015
*/

/**
* @file multimat.java
* @brief Contiene la clase multimat y el programa principal
*
* Las funciones para encontrar el orden óptimo de multiplicación, realizar
* la multiplicación y generar los datos de salida están implimentados en 
* este archivo. 
*/

import java.util.Arrays;
import java.io.*;

/**
* @class multimat
* Clase que contiene los métodos para encontrar el orden óptimo de
* multiplicación
*/
public class multimat {
	
	private Matrix[] matrices_;
	private int[] dimensions_;
	private int[][] minCosts_;
	private int[][] optMidPoints_;
	private int totalOps_;
	private String parenthesis_;
	private boolean[] printed_;
	private Matrix resultMatrix_;
	private boolean trace_;
	private boolean writeToFile_;
	private File outputFile_;
	private PrintWriter writer_;

        /**
        * Constructor
        * @param inputMatrices Array de matrices leidas del fichero de entrada
        */
	public multimat(Matrix[] inputMatrices,boolean trace,String outputFile) {
		// Inicializa el array de matrices y el array con sus
		// dimensiones
		matrices_=inputMatrices;
		dimensions_=new int[matrices_.length+1];
		for (int i=0;i<matrices_.length;i++){
			//Ai tiene dimensiones d[i]xd[i+1]
			dimensions_[i]=matrices_[i].getM(); 
		}
		dimensions_[matrices_.length]=
			matrices_[matrices_.length-1].getN();
		// Variables usadas para imprimir la asociación
		parenthesis_=new String("");
		printed_=new boolean[matrices_.length];
		// Matriz resultado
		Matrix finalProduct;

		// Abre el fichero de salida si es necesario
		if (outputFile!=null){
		        outputFile_= new File(outputFile);
			writeToFile_=true;
		        try {
				writer_= new PrintWriter(outputFile_,"UTF-8"); 
			}catch ( IOException e ) {
          			 e.printStackTrace();
			}
		}
		else{
			 writeToFile_=false;
		}

		// Lee la opción traza 
		trace_=trace;
		printTraceStart();

		// Establece el orden óptimo de multiplicación
		orderMatrices();
		// Modifica el string de la asociación basado en el resultado
		// óptimo
		genParenthesis(0,matrices_.length-1);	
		// Realiza la multiplicación
		finalProduct=(findProduct(0,matrices_.length-1));
		printTraceEnd();
		printOutput(finalProduct);


	}

        /**
        * Calcula el coste mínimo para la multiplicación de cualquier subgrupo
	* de matrices de la lista (manteniendo el orden) y la matriz k que 
	* divide el subgrupo produciendo dicho coste. El cálculo de realiza
	* incrementando secuencialmente el tamaño de los subgrupos, calculando
	* en última instancia el coste mínimo y la matriz k para la lista 
	* completa, lo que genera la solución buscada.
        */
	public void orderMatrices(){
		// Inicializa las matrices con el coste mínimo y el valor de k
		// para cada par de elementos en la lista
		int n = matrices_.length;
		minCosts_=new int [n][n];
		optMidPoints_=new int[n][n];	
		// Multiplicar subgrupos de 1 elemento tiene coste 0		
		for (int i=0; i<n;i++){
			minCosts_[i][i]=0;
			for (int j=0; j<n;j++){
				optMidPoints_[i][j]=-1;
			}
		}
		// Para subgrupos de tamaño 2 hasta n-1
		for (int l=1;l<n;l++){
			// Para los subgrupos posibles con tamaño l
			for (int i=0;i<(n-l);i++){
				int j=i+l;
				// Asignar coste inicial infinito
				minCosts_[i][j]=Integer.MAX_VALUE;
				// Encuentra el valor de k que da coste
				// mínimo
				for (int k=i;k<j;k++){
					// q es el coste mínimo de multiplicar 
					// el subgrupo i,j
					int q=minCosts_[i][k]+minCosts_[k+1][j]
					      +dimensions_[i]*dimensions_[k+1]
					      *dimensions_[j+1];
					if (q<minCosts_[i][j]){
						minCosts_[i][j]=q;
						optMidPoints_[i][j]=k;
					}
				}
				if (trace_) printTrace();
			}
		}
	}
	


        /**
        * Modifica el string que indica la asociación óptima de cualquier
	* subgrupo de matrices de la lista (manteniendo el orden), incluyendo
	* la lista completa. Se basa en las mátrices de coste óptimo y valores 
	* óptimo de k. 
	* Para cada par i,j escribe un paréntesis izquierdo, haces dos llamadas
	* recursivas para imprimir los parentesis de los conjuntos entre i y k
	* y entre k+1 y j y finalmente escribe un parentesis izquierdo.
	* Usa un vector de booleans para asegurar que no se incluye dos veces
	* la misma matriz
	* @param i Indica el elemento inicial del subgrupo
	* @param j Indica el elemento final del subgrupo
        */
	public void genParenthesis(int i, int j){
		//Si solo hay un elemento no se hace nada
		if (i!=j){
			parenthesis_+="(";
			// Llamada recursiva para la parte izquierda
			// (desde i hasta k)
			genParenthesis(i,optMidPoints_[i][j]);
			String iStr= printed_[i] ? "" : "M"+i;	
			parenthesis_+=(iStr + " x ");
			// Llamada recursiva para la parte derecha
			// (desde k+1 hasta j)		
			genParenthesis(optMidPoints_[i][j]+1,j);
			String jStr= printed_[j] ? "" : "M"+j;
			parenthesis_+=(jStr+")");
			// Marca i y j como ya impresos
			printed_[i]=printed_[j]=true;
		}
	}

        /**
        * Realiza la multiplicación de cualquier subgrupo de matrices de la 
	* lista (manteniendo el orden), incluyendo la lista completa y
	* siguiendo la asociación óptima.
	* Divide el grupo según el valor de k y hace llamadas recursivas para
	* multiplicar cada uno de los elementos resultantes (i,k y k+1,j).
	* @param i Indica el elemento inicial del subgrupo
	* @param j Indica el elemento final del subgrupo
	* @return C Matriz resultado
	*/
	public Matrix findProduct(int i, int j){
		// 
		if (i!=j){
			if (j==(i+1)){
				return multiply(matrices_[i],matrices_[j]);
			}
			else{
				return multiply(
					findProduct(i,optMidPoints_[i][j]),
					findProduct(optMidPoints_[i][j]+1,j));
			}
		}	
		return matrices_[i];
	}

        /**
        * Multiplica dos matrices A y B de dimensiones MxN y NxP. Si las
	* dimensiones no son correctas aborta el programa con un error.
	* @param A Primera matriz de la multiplicación
	* @param B Segunda matriz de la multiplicación
	* @return C Matriz resultado
	*/
	private Matrix multiply(Matrix A,Matrix B) {
		int M = A.getM();
		int N = A.getN();
		int P = B.getN();
		if(N != B.getM()){ 
			throw new IllegalArgumentException(
			"Las dimensiones de las matrices no son validas");
		}
		float[][] valsA=A.getValues();
		float[][] valsB=B.getValues();
		float[][] valsC = new float[M][P];
		for(int i=0;i<M;i++){
			for(int j=0;j<P;j++){
				for(int k=0;k<N;k++){
					valsC[i][j]+=valsA[i][k]*valsB[k][j];
				}
			}
		}
		Matrix C = new Matrix(valsC,M,P);
		return C;
	}

        /**
        * Imprime todos los valores de una matriz dada. Cada fila se imprime
	* en una linea y los valores dentro de cada fila se separan por un 
	* espacio en blanco.
	* @param A Matriz a imprimir
	*/
	private void printMatrix(Matrix A){
		String s= new String("");
		float[][] values= A.getValues();
		for (int i=0;i<A.getM();i++){
			for (int j=0;j<A.getN();j++){
				s+=(values[i][j]+" ");
			}
			s+="\n";

		}
		if (writeToFile_){
			writer_.print(s);
		}

		else{
			System.out.print(s);
		}
	}
        /**
	* Imprime el resultado final del programa en el formato especificado:
	* -Linea con la asociación óptima
	* -Linea con el número de operaciones
	* -Descripción de la matriz resultado
	* @param finalProduct Matriz resultado
	*/
	private void printOutput(Matrix finalProduct){
		totalOps_=minCosts_[0][matrices_.length-1];
			if (writeToFile_){
				writer_.print("Asociación: "+
						parenthesis_+"\n");
				writer_.print("Operaciones: "+
						totalOps_+"\n");
				writer_.print(finalProduct.getM()+
						" "+finalProduct.getN()+"\n");
				printMatrix(finalProduct);
				writer_.close();
			}
			else{
				System.out.println("Asociación: "+
							parenthesis_);	
				System.out.println("Operaciones: "+
								totalOps_);
				System.out.println(finalProduct.getM()+
						" "+finalProduct.getN());
				printMatrix(finalProduct);
			}	
	}

        /**
        * Imprime la traza del calculo. Este metodo es llamado cada vez que
	* se encuentra el valor k ótimo para un nuevo par de matrices i,j.
	* Imprime unicamente los valores de la diagonal superior, llenando
	* el resto de la matriz con la cadena "--". Los valores desconocidos
	* hasta el momento se indican con la cadena "??"
	*/
	private void printTrace(){
		String s=new String("");
		for (int i=0; i<matrices_.length;i++){
			for (int j=0;j<=i;j++){
				s+=("-- ");
			}
			for (int j=i+1;j<matrices_.length;j++){
				if (optMidPoints_[i][j]==-1){
					s+=("?? ");
				}
				else{
					s+=("M"+optMidPoints_[i][j]+" ");
				}
			}
			s+=("\n");
		}
		s+=("\n");
		if (writeToFile_){
			writer_.print(s);
		}
		else{
			System.out.print(s);
		}
	}

        /**
        * Imprime el comienzo de la traza
	*/
	private void printTraceStart(){
		if (trace_){						
			if (writeToFile_){
				writer_.print("Traza:\n");
			}
			else{
				System.out.print("Traza:\n");
			}
		}
	}

        /**
        * Imprime el final de la traza
	*/
	private void printTraceEnd(){
		if (trace_){						
			if (writeToFile_){
				writer_.print("Fin de traza\n\n");
			}
			else{
				System.out.print("Fin de traza\n\n");
			}
		}
	}

        /**
        * Programa principal. Crea una instancia del lector de datos de 
	* entrada, con los argumentos del programa y recibe de este la lista
	* de matrices entrada y el estado de la opción traza.
	* Crea una instancia de multimat con la lista de matrices de entrada
	* y el valor de la opción traza e invoca sus métodos para calcular
	* la solución del problema y generar los datos de salida.
	* @param args Argumentos con los que se ha ejecutado el programa
	*/
	public static void main(String[] args) {
		InputHandler IHandler = new InputHandler(args);
		Matrix[] inputMatrices=IHandler.getMatrices();
		boolean trace=IHandler.getTrace();
		String outputFile=IHandler.getOutFile();
		multimat solver = new multimat(inputMatrices,trace,outputFile);		
	}
}
