/**
* @file InputHandler.java
* @brief Contiene la clase InputHandler 
*
* Esta clase recibe los argumentos con los que se ha ejecutado el programa,
* muestra la ayuda del programa si se especifica, lee los datos de entrada
* y genera un array con las matrices descritas en el fichero de entrada. 
* Además genera errores si los argumentos no son válidos, si el fichero de
* entrada no existe o si ya existe el fichero de salida especificado
*/

import java.util.Arrays;
import java.io.*;

/**
* @class InputHandler
* Lee datos de entrada e interpreta las opciones del programa. Verifica
* que todos los parámetros de entrada sean válidos.
*/
public class InputHandler {

	private Matrix[] matrices_;
	private boolean trace_;
	private boolean showHelp_;	
	
	public InputHandler(String[] args) {
		String inputFile=args[0];
		readFile(inputFile);
	}		

        /**
	* Lee la lista de matrices del fichero de datos de entrada y genera
	* un vector de matrices a partir de ellos. Genera errores si el 
	* fichero de entrada no existe o no se puede leer.
	* @param inputFile Nombre del archivo de datos de entrada
        */
	private void readFile(String inputFile){
		BufferedReader br = null;
		Matrix[] matricesList;
		float[][] matrixValues;

 		try {
 			String sCurrentLine;
 			br = new BufferedReader(new FileReader(inputFile));
			sCurrentLine = br.readLine();
			String[] lineVals = sCurrentLine.split(" ");
			int nMatrices=Integer.parseInt(lineVals[0]);
			matricesList=new Matrix[nMatrices];
			for (int i=0; i<nMatrices;i++){
				sCurrentLine = br.readLine();
				lineVals = sCurrentLine.split(" ");
				int M = Integer.parseInt(lineVals[0]);	
				int N = Integer.parseInt(lineVals[1]);
				matrixValues= new float[M][N];
				for (int j=0;j<M;j++){
					sCurrentLine = br.readLine();
					lineVals = sCurrentLine.split(" ");
					for (int k=0;k<N;k++){
						matrixValues[j][k]=Float.parseFloat(lineVals[k]);
					}
				}
				matricesList[i]=new Matrix(matrixValues,M,N);
			}
			matrices_=matricesList;
 		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

        /**
        * Devuelve el vector con las matrices leidas del fichero de entrada
	* @return matrices Vector de matrices  
        */
	public Matrix[] getMatrices(){
		return matrices_;
	}
}
