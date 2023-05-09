/**
 * @author Sabastian Highton
 * 1) This was for a Data Structures class not for this project but it can be useful
 * 2) To use it for this project -> Have one line be the concatenated sequences and the next be the dot-parenthesis notation
 * 3) Results are stored in the Results.txt file
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class rnaFoldStack {
	public static void main(String[] args){
		BufferedReader br;
		String everything = "";
		String dotBrackIn = "";
		String rnaSeqIn = "";
		List<String> dotBrack = new ArrayList<String>();
		List<String> rnaSeq = new ArrayList<String>();

		try {
			br = new BufferedReader(new FileReader("/Users/shighton/IdeaProjects/rnaML2/src/lib/data_for_stack_project1.txt"));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    everything = sb.toString();
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int i = 0; i < everything.length(); i++) {
			if(String.valueOf(everything.charAt(i)).equalsIgnoreCase("A") || String.valueOf(everything.charAt(i)).equalsIgnoreCase("C") || String.valueOf(everything.charAt(i)).equalsIgnoreCase("G") || String.valueOf(everything.charAt(i)).equalsIgnoreCase("U")) {
				rnaSeqIn = rnaSeqIn.concat(String.valueOf(everything.charAt(i)));
			}
			else if(String.valueOf(everything.charAt(i)).equalsIgnoreCase("\n")) {
				rnaSeqIn = rnaSeqIn.concat(String.valueOf(everything.charAt(i)));
				dotBrackIn = dotBrackIn.concat(String.valueOf(everything.charAt(i)));
			}
			else if(String.valueOf(everything.charAt(i)).equals("(") || String.valueOf(everything.charAt(i)).equals(".") || String.valueOf(everything.charAt(i)).equals(")")) {
				dotBrackIn = dotBrackIn.concat(String.valueOf(everything.charAt(i)));
			}
		}
		for(int i = 0; i < dotBrackIn.length(); i++){
			dotBrack.add(String.valueOf(dotBrackIn.charAt(i)));
		}

		for(int i = 0; i < rnaSeqIn.length(); i++){
			rnaSeq.add(String.valueOf(rnaSeqIn.charAt(i)));
		}

		bondingProbabilities(dotBrack, rnaSeq);
	}
	public static void bondingProbabilities(List<String> dotBrack, List<String> rnaSeq){
		Stack<String> bondType = new Stack<String>();
		Stack<String> bondPairs = new Stack<String>();
		DecimalFormat df = new DecimalFormat("0.00");
		int close_index = 0;
		int open_index = 0;
		String temp_pop;
		int bondCount = 0;
		int bondAU_count = 0;
		int bondCG_count = 0;
		int bondAC_count = 0;
		int bondAG_count = 0;
		int bondCA_count = 0;
		int bondCU_count = 0;
		int bondUG_count = 0;
		int bondAA_count = 0;
		int bondUU_count = 0;
		int bondCC_count = 0;
		int bondGG_count = 0;
		int bond_mystery = 0;
		for(int i = 0; i < dotBrack.size(); i++){
			if(dotBrack.get(i).equals("(")){
				bondType.push(String.valueOf(i));
				bondCount++;
			}
			else if(dotBrack.get(i).equals(")")){
				close_index = i;
				open_index = (Integer.parseInt(bondType.pop()));
				bondPairs.push(rnaSeq.get(open_index) + " " + rnaSeq.get(close_index));
			}
		}
		while(!bondPairs.isEmpty()){
			temp_pop = bondPairs.pop();
			if(temp_pop.equalsIgnoreCase("A U") || temp_pop.equalsIgnoreCase("U A")) {
				bondAU_count++;
			}
			else if(temp_pop.equalsIgnoreCase("C G") || temp_pop.equalsIgnoreCase("G C")) {
				bondCG_count++;
			}
			else if(temp_pop.equalsIgnoreCase("A C") || temp_pop.equalsIgnoreCase("C A")) {
				bondAC_count++;
			}
			else if(temp_pop.equalsIgnoreCase("A G") || temp_pop.equalsIgnoreCase("G A")) {
				bondAG_count++;
			}
			else if(temp_pop.equalsIgnoreCase("C A") || temp_pop.equalsIgnoreCase("A C")) {
				bondCA_count++;
			}
			else if(temp_pop.equalsIgnoreCase("C U") || temp_pop.equalsIgnoreCase("U C")) {
				bondCU_count++;
			}
			else if(temp_pop.equalsIgnoreCase("G U") || temp_pop.equalsIgnoreCase("U G")) {
				bondUG_count++;
			}
			else if(temp_pop.equalsIgnoreCase("A A")) {
				bondAA_count++;
			}
			else if(temp_pop.equalsIgnoreCase("U U")) {
				bondUU_count++;
			}
			else if(temp_pop.equalsIgnoreCase("C C")) {
				bondCC_count++;
			}
			else if(temp_pop.equalsIgnoreCase("G G")) {
				bondGG_count++;
			}
			else{
				bond_mystery++;
			}
		}

		File aFile = new File("/Users/shighton/IdeaProjects/rnaML2/src/lib/Results.txt");
		try {
			if(aFile.createNewFile()){
				System.out.println("File creation successful.");
			}
			else{
				System.out.println("Unable to create file.");
			}
		} catch (IOException e) {
			System.out.println("Error creating file.");
			e.printStackTrace();
		}

		double auPer = ((double)bondAU_count/bondCount);
		double cgPer = ((double)bondCG_count/bondCount);
		double ugPer = ((double)bondUG_count/bondCount);

		FileWriter writer;
		try {
			writer = new FileWriter("/Users/shighton/IdeaProjects/rnaML2/src/lib/Results.txt");
			writer.write("Total Number of Bonds: " + bondCount);
			writer.write("\nAU: " + bondAU_count);
			writer.write("\nCG: " + bondCG_count);
			writer.write("\nAC: " + bondAC_count);
			writer.write("\nAG: " + bondAG_count);
			writer.write("\nCA: " + bondCA_count);
			writer.write("\nCU: " + bondCU_count);
			writer.write("\nUG: " + bondUG_count);
			writer.write("\nAA: " + bondAA_count);
			writer.write("\nUU: " + bondUU_count);
			writer.write("\nCC: " + bondCC_count);
			writer.write("\nGG: " + bondGG_count);
			writer.write("\nUnknown Bonds: " + bond_mystery);
			writer.write("\nPercent AU: " + df.format(auPer));
			writer.write("\nPercent CG: " + df.format(cgPer));
			writer.write("\nPercent GU: " + df.format(ugPer));
			writer.close();
			System.out.println("File writing success.");
		} catch (IOException e) {
			System.out.println("Error writing to file.");
			e.printStackTrace();
		}
	}
}



