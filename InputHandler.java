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
	private boolean help_;
	private String inputFile_,outputFile_;	
	
	public InputHandler(String[] args) {
		if (args.length>4){
			throw new IllegalArgumentException(
				"Demasiados argumentos (>4)!");
		}
		
		int nOptions=0;
		int nArguments=args.length;

		for (int i=0;i<args.length;i++){
			if (args[i].equals("-h")){
				help_=true;
				showHelp();
				System.exit(0);
			}
			if (args[i].equals("-t")){
				trace_=true;
				nOptions++;
			}
		}

		nArguments=args.length-nOptions;
		if (nArguments>2){
			throw new IllegalArgumentException(
				"Argumentos invalidos");
		}
		inputFile_=args[nOptions];
		outputFile_=null;
		if (nArguments==2){
			outputFile_=args[nOptions+1];
			testOutput();
		}

		testInput();
		readFile(inputFile_);
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
			checkMatrices();
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
	* Comprueba que las matrices de entrada se puedan multiplicar
        */
	public void checkMatrices(){
		for (int i=0;i<matrices_.length-1;i++){
			if (matrices_[i].getN()!=matrices_[i+1].getM()){
				throw new IllegalArgumentException(
				"Las dimensiones de las matrices no "+
				"son validas");
			}
		}
	}				

        /**
	* Comprueba que el fichero de entrada exista y que no sea un directorio
        */
	public void testInput(){
		File f = new File(inputFile_);
			if(!f.exists() || f.isDirectory()){
				throw new IllegalArgumentException(
				"El fichero de entrada no existe");
			}
	}

        /**
	* Comprueba que el fichero de salida no exista y que no sea un 
	* directorio
        */
	public void testOutput(){
		File f = new File(outputFile_);
			if(f.exists() && !f.isDirectory()){
				throw new IllegalArgumentException(				
				"El fichero de salida ya existe");
			}
	}

        /**
        * Devuelve el vector con las matrices leidas del fichero de entrada
	* @return matrices Vector de matrices  
        */
	public Matrix[] getMatrices(){
		return matrices_;
	}

        /**
        * Devuelve el estado de la opcion traza
	* @return trace
        */
	public boolean getTrace(){
		return trace_;
	}

        /**
        * Devuelve el estado de la opcion ayuda
	* @return help
        */
	public boolean getHelp(){
		return help_;
	}

        /**
        * Devuelve el nombre del fichero de salida
	* @return outputFile
        */
	public String getOutFile(){
		return outputFile_;
	}

        /**
        * Muestra la ayuda del programa
        */
	public void showHelp(){
		System.out.print("\nSINTAXIS:\n");
		System.out.print("multimat [-t] [-h] [fichero_entrada] [fichero_salida]\n");
		System.out.print("-t\t\tTraza la selección del orden de multiplicación\n");
		System.out.print("-h\t\tMuestra esta ayuda\n");
		System.out.print("fichero_entrada\tNombre del fichero de entrada\n");
		System.out.print("fichero_salida\tNombre del fichero de salida\n\n");
	}
}
