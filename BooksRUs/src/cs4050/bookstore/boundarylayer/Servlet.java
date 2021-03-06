package cs4050.bookstore.boundarylayer;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs4050.bookstore.logiclayer.*;
import cs4050.bookstore.objectlayer.*;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

		Configuration cfg = null;

		private String templateDir = "/WEB-INF/templates";
		String currentUser;


		/**
		 * @see HttpServlet#HttpServlet()
		 */
		public Servlet() {
			super();
		}

		public void init() {
			// Create your Configuration instance, and specify if up to what FreeMarker
			// version (here 2.3.25) do you want to apply the fixes that are not 100%
			// backward-compatible. See the Configuration JavaDoc for details.
			cfg = new Configuration(Configuration.VERSION_2_3_25);

			// Specify the source where the template files come from.
			cfg.setServletContextForTemplateLoading(getServletContext(), templateDir);

			// Sets how errors will appear.
			// This handler outputs the stack trace information to the client, formatting it so 
			// that it will be usually well readable in the browser, and then re-throws the exception.
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		}

		/*
		 * This method is called by the doGet method. This method will create and process the template
		 * used to display the search results or an error page if the user input is invalid.
		 */
		public void runTemplate(HttpServletRequest request, HttpServletResponse response) throws SQLException {

			Template template = null;
			String templateName = null; //template to be generated

			DefaultObjectWrapperBuilder df = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
			SimpleHash root = new SimpleHash(df.build());
			
			// The following Strings are used to check for a null value. Whichever string that does not have a null value is the action the client wants to perform
			String register = request.getParameter("register"); // sign up page's "Register" button
			String login = request.getParameter("login"); // login page's "Login" button

			
			
			String addToCart = request.getParameter("");
			String removeFromCart = request.getParameter("");
			String editProfileInfo = request.getParameter("");
			String deleteAccount = request.getParameter("");
			String enterPromo = request.getParameter("");
			String order = request.getParameter("");

			//vendors and admin operations below
			String addBook = request.getParameter("");
			String removeBook = request.getParameter("");
			String getSalesReport = request.getParameter("");
			String get = request.getParameter("");
			
			
			
			
			String about = request.getParameter("about"); // home page "About Us" button
			String leaseMyApartment = request.getParameter("leaseMyApartment"); // home page "Lease your apartment" button
			String checkMessages = request.getParameter("checkMessages"); // home page "Inbox" button
			String leaseIt = request.getParameter("leaseIt"); // submit their lease button
			String loginName = null;


			//begin checks to see what the input is
			if (register!= null){ // check to see if user clicked the register button on the sign up page
				String email = request.getParameter("email");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String fname = request.getParameter("fname");
				String lname = request.getParameter("lname");
				
				User user = new User(fname, lname, email, username, password, 2);
				UserLogicImpl newUser = new UserLogicImpl();
				int r = newUser.insertUser(user);
				
				if (r == 0){
					templateName = "signup.ftl"; //error inserting the new user into the database.
					root.put("registerError","yes");

				} else{
					root.put("registerSuccessful","yes");
					templateName = "login.ftl";
				}
				
				templateName = "login.ftl";
				
			} else if (login != null){
				String username = request.getParameter("username");
				String password = request.getParameter("password");

				UserLogicImpl existingUser = new UserLogicImpl();
				boolean authenticUser = existingUser.authenticateUser(username, password);
				
				if(authenticUser == false){ //enter here if authentification fails
					templateName = "login.ftl"; 
					root.put("failedLogin"," yes");
				} else{
					templateName = "home.ftl";
				}
				
			}
				


				
				
				
				
				
				
	
			try {
				template = cfg.getTemplate(templateName);
				response.setContentType("text/html");
				Writer out = response.getWriter();
				template.process(root, out); 
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
		}
		
		

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				try {
					runTemplate(request, response);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
		}
}
