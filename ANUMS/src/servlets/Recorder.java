package servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Recorder
 */
@WebServlet("/Recorder")
public class Recorder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Recorder() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] url=request.getParameterValues("url");
		String[] relevance=request.getParameterValues("relevance");
		if(url!=null&&relevance!=null)
		{
			String filePath=getServletContext().getRealPath("/Results");     
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Calendar cal = Calendar.getInstance();
            File file = new File(filePath, request.getParameter("query")+"_"+dateFormat.format(cal.getTime())+".txt");     
            if (!file.getParentFile().exists()) {  
                if (file.getParentFile().mkdirs());  
                else  
                    System.out.println("Unable to create folder");  
            }  
              
            if (!file.exists()) {  
//                System.out.println(String.format("Creating file %s...", file.getAbsolutePath()));  
                if (file.createNewFile())  
                {
                    ;
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
			for(int i=0;i<url.length;i++)
			{
     			bw.write("[Document]"+url[i]+"[Relevance]"+relevance[i]+"\n");
			}
			bw.close();
		}
		HttpSession session = request.getSession(true);
//		System.out.println("next:"+request.getParameter("index"));
//		System.out.println("size:"+session.getAttribute("querySize").toString());
		if(request.getParameter("index").trim().equals(session.getAttribute("querySize").toString()))
		{
			RequestDispatcher rd = request.getRequestDispatcher("/Thank.jsp");
			rd.forward(request, response);
		}
		else
		{
		request.setAttribute("index", request.getParameter("nextIndex"));
		RequestDispatcher rd = request.getRequestDispatcher("/documentJudge.jsp");
		rd.forward(request, response);
		}
	}
}
