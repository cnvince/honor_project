package servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.broker.Controller;
import com.datatype.ALGORITHM;
import com.results.Result;


/**
 * Servlet implementation class config
 */
@WebServlet("/config")
public class config extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public config() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fileName = "/Queries/queries.txt";
		ServletContext context = getServletContext();
		InputStream ins = context.getResourceAsStream(fileName);
		//read from file
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        String cquery;

        while((cquery = reader.readLine())!= null){
            String query=cquery.trim();
            String filePath=getServletContext().getRealPath("/Queries");     
            File file = new File(filePath, query+".txt");     
            if (!file.getParentFile().exists()) {  
                System.out.println(String.format("Creating folder %s...", file.getParentFile().getAbsolutePath()));  
                if (file.getParentFile().mkdirs())  
                    System.out.println("Done");  
                else  
                    System.out.println("Unable to create folder");  
            }  
              
            if (!file.exists()) {  
                System.out.println(String.format("Creating file %s...", file.getAbsolutePath()));  
                if (file.createNewFile())  
                {
                    System.out.println("Done");
                }
                else  
                {
                    System.out.println("Unable to create file");  
                }
            } 
            //if the file is already existed, update
            else  
            {
            	 FileWriter fw = new FileWriter(file.getAbsoluteFile());
     			BufferedWriter bw = new BufferedWriter(fw);
     			bw.write("");
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			ArrayList<Result> results = new ArrayList<Result>();
			Controller controller = new Controller();
			results = controller.fetchResult(query, ALGORITHM.JUDGE);
			long seed = System.nanoTime();
			Collections.shuffle(results, new Random(seed));
			for (Result result : results) {
				String Title=result.getTitle().replaceAll("\\s+", " ").trim();
				bw.append("[Title]"+Title + "[Link]" + result.getLink()+"[Source]"+result.getSourceName()+"\n");
			}
			bw.close();
           
        }
        RequestDispatcher rd = request.getRequestDispatcher("Initial.jsp");
//        request.setAttribute("msg","HI Welcome");
        rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
