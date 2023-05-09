import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class grabLabels {
	public static void main(String[] args) {
		BufferedReader br;
		String all = "";
		try {
			br = new BufferedReader(new FileReader("/Users/shighton/eclipse-workspace/rnaML/src/rnaML/MHL_hsa_sep29_trnSet_miRNAgrpby_20_80.arff"));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    all = sb.toString();
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pattern patternMiss = Pattern.compile("0\\s\\%\\s\\w{22}\\&\\w{25}");
		Matcher mMiss = patternMiss.matcher(all);
		String sMiss = "";
		while(mMiss.find()) {
			sMiss = sMiss + mMiss.group() + "&\n";
		}
		Pattern patternHit = Pattern.compile("1\\s\\%\\s\\w{22}\\&\\w{25}");
		Matcher mHit = patternHit.matcher(all);
		String sHit = "";
		while(mHit.find()) {
			sHit = sHit + mHit.group() + "&\n";
		}
		
		Pattern onlyMiss = Pattern.compile("\\w{22}\\&\\w{25}");
		Matcher mMisses = onlyMiss.matcher(sMiss);
		String sMisses = "";
		while(mMisses.find()) {
			sMisses = sMisses + mMisses.group() + "&\n";
		}
		Pattern onlyHits = Pattern.compile("\\w{22}\\&\\w{25}");
		Matcher mHits = onlyHits.matcher(sHit);
		String sHits = "";
		while(mHits.find()) {
			sHits = sHits + mHits.group() + "&\n";
		}
		
		File fileMiss = new File("/Users/shighton/eclipse-workspace/rnaML/src/rnaML/dataForMissPic.txt");
		try {
			FileWriter myWriter = new FileWriter(fileMiss);
			myWriter.write(sMisses);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to create the file");
		}
		File fileHit = new File("/Users/shighton/eclipse-workspace/rnaML/src/rnaML/dataForHitPic.txt");
		try {
			FileWriter myWriter = new FileWriter(fileHit);
			myWriter.write(sHits);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to create the file");
		}
		
	}
}
