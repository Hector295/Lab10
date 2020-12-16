package Controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import Beans.Employee;
import Daos.DepartmentDao;
import Daos.EmployeeDao;
import Daos.JobDao;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "login" : request.getParameter("action");


        switch (action) {
            case "login":
                String Email = request.getParameter("inputEmail");
                String Password = request.getParameter("inputPassword");

                EmployeeDao employeeDao = new EmployeeDao();

                Employee employee = employeeDao.validarUsuarioPasswordHash(Email, Password);

                if (employee != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("employee", employee);
                    if (employee.getJob().getMaxSalary()>15000){
                        session.setAttribute("top",1);
                    } else if (employee.getJob().getMaxSalary()<=15000 &&
                            employee.getJob().getMaxSalary()>8500){
                        session.setAttribute("top",2);
                    } else if (employee.getJob().getMaxSalary()<=8500 &&
                            employee.getJob().getMaxSalary()>5000){
                        session.setAttribute("top",3);
                    }else if(employee.getJob().getMaxSalary()<=5000){
                        session.setAttribute("top",4);
                    }
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } else {
                    response.sendRedirect(request.getContextPath() + "/LoginServlet?error");
                }
                break;
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "login" : request.getParameter("action");

        HttpSession session = request.getSession();

        switch (action) {
            case "logout":
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                break;
            case "login":
                Employee employee = (Employee) session.getAttribute("employee");
                if (employee != null && employee.getEmployeeId() > 0) {
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("includes/index.jsp");
                    rd.forward(request, response);
                }
                break;
        }
    }
}
