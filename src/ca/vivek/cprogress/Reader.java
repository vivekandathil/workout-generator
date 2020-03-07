package ca.vivek.cprogress;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Reader 
{
	public String getPDF(int n, String[] muscles) throws IOException
	{
		//TODO: Read directly from OneDrive rather than local file
		List<Exercise> lines = new ArrayList<>();
		
		BufferedReader in = Files.newBufferedReader(Paths.get("/Users/vivekkandathil/Documents/Calisthenics_Progressions.csv"));
		
		// The table headers
		String[] headers = in.readLine().split(",");
		
		String line = in.readLine();
		
		// Add an exercise object for each entry
		while (line != null)
		{
			String[] attributes = line.split(",");
			
			lines.add(generateExercise(attributes));
			line = in.readLine();
		}
		
		String start = startLatex(n);
		String tabular = generateLatexBody(lines, n, muscles);
		String end = "\n\\end{document}";
		
		String total = start + tabular + end;
		String encoded = "https://latexonline.cc/compile?text=" + java.net.URLEncoder.encode(total, StandardCharsets.UTF_8.toString());
		
		System.out.println(encoded);
		
		return encoded;


	}
	
	private static Exercise generateExercise(String[] a) throws IllegalArgumentException
	{
		// I might have included an extra comma in the notes somewhere that messed up the split
		if (a.length != 9)
			throw new IllegalArgumentException("An error occured with readng the excel data");
		
		String type = a[0];
		int weight = Integer.parseInt(a[1]);
		String date = a[3];
		String[] sets = a[4].split("x");
		String[] progressions = {a[5], a[6]};
		String notes = a[7];
		String[] muscles = a[8].split("-");
		
		String max = a[2];
		int maximum = 0;
		// To see whether it is a timed exercise
		boolean timed = max.charAt(max.length() - 1) == 's';
		
		try
		{
			if (timed)
				maximum = Integer.parseInt(max.substring(0,max.length() - 1));
			else
				maximum = Integer.parseInt(max);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Couldn't cast string to int: " + max);
			e.printStackTrace();
		}
		
		return new Exercise(type, weight, maximum, date, sets, progressions, notes, timed, muscles);
	}
	
	private static String generateLatexBody(List<Exercise> lines, int n, String[] muscles)
	{
		String table = "\\begin{table}[H]\n" + 
				"\\begin{tabular}{lllllll}\n" + 
				"\\hline\n" + 
				"\\textbf{Name}    "
				+ "& \\textbf{Weight (lbs)} "
				+ "& \\textbf{Sets} "
				+ "& \\multicolumn{1}{l}{\\textbf{Reps/Time}} "
				+ "& \\multicolumn{1}{l}{\\textbf{Current Progression}} "
				+ "& \\multicolumn{1}{l}{\\textbf{Next Progression}}\\\\ \\hline\n";
		String body = "Target Muscles: " + Arrays.toString(muscles);
		
		List<Exercise> toUse = new ArrayList<>();
		List<Exercise> filtered = new ArrayList<>();
		List<String> usedCodes = new ArrayList<>();
		
		Map<String,String> codes = new HashMap<>();
		codes.put("PU", "Bodyweight Push");
		codes.put("PN", "Bodyweight Pull");
		codes.put("PL", "Planche Training");
		codes.put("AG", "Miscellaneous");
		codes.put("WC", "Weights (Chest)");
		codes.put("WB", "Weights (Back)");
		codes.put("WS", "Weights (Shoulder)");
		codes.put("WA", "Weights (Arms)");
		codes.put("GR", "Grip Strength");
		codes.put("RG", "Rings");
		
		for (Exercise exercise : lines)
		{
			for (String muscle : muscles)
			{
				if (Arrays.asList(exercise.getMuscles()).contains(muscle))
				{
					toUse.add(exercise);
				}
			}
			
		}
		
		System.out.println(n + " exercises randomly chosen:\n");
		
		Random t = new Random();
		int randIndex;
		
		// Select random exercises form the list and remove them from the
		// originsl list to ensure there aren't any repetitions
		for (int i = 0; i < 6; i++)
		{
			randIndex = t.nextInt(toUse.size());
			Exercise temp = toUse.get(randIndex);
			filtered.add(temp);
			System.out.println(temp.getType());
			//System.out.println(exercise.toString());
			if (!usedCodes.contains(temp.getCode()))
				usedCodes.add(temp.getCode());
			toUse.remove(randIndex);
		}
		
		Map<String,String> notes = new HashMap<>();
		
		for (String code : usedCodes)
		{
			System.out.println(codes.get(code));
			body += "\\subsection*{" + codes.get(code) + "}\n" + table;
			for (Exercise a : filtered)
			{
				if (code.equals(a.getCode()))
				{
					body += a.getType().substring(4) + "&" + a.getWeightAdded() + "&" + a.getSetsReps()[0] + "&" + a.getSetsReps()[1] + "&" + a.getProgressions()[0] + "&" + a.getProgressions()[1] + "\\\\";
					if (!a.getNotes().equals("."))
						notes.put(a.getType().substring(4),a.getNotes());
				}
			}
			body += "\\end{tabular}\n" + "\\end{table}\n";
			if (!notes.isEmpty())
			{
				body+= "\\textbf{Notes:}\n\\begin{itemize}\n";
				
				for (String b : notes.keySet())
				{
					body += "\\item " + b + " $\\rightarrow$ " + notes.get(b) + "\n";
				}
				
				body += "\\end{itemize}\n";
			}
			
			notes.clear();
		}
		
		return body;
	}
	
	private static String startLatex(int n)
	{
		return "\\documentclass{article}\n" + 
				"\\usepackage[margin=0.6in]{geometry}\n" + 
				"\\usepackage[utf8]{inputenc}\n" + 
				"\\usepackage{hyperref}\n" + 
				"\\usepackage{float}" +
				"\n" + 
				"\\title{Generated Workout}\n" + 
				"\\date{\\vspace{-5ex}}\n" + 
				"\\begin{document}\n" + 
				"\n" + 
				"\\maketitle\n" + 
				"\\noindent This LaTeX document was generated programmatically on " 
				+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) 
				+ " using a random selection of " + Integer.toString(n) + " weight\\\\ and calisthenics "
				+ "exercises from Vivek's Excel spreadsheet (\\url{bit.ly/2wAu1AB}). "
				+ "(The order is completely random!)\\\\\\\\\n";
	}

}
