package Controllers;

import Beans.Country;
import Beans.Department;
import Beans.Employee;
import Daos.CountryDao;
import Daos.DepartmentDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet(name = "DepartmentServlet", urlPatterns = {"/DepartmentServlet"})
public class DepartmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        DepartmentDao departmentDao = new DepartmentDao();
        RequestDispatcher view;
        Department department;
        int departmentId;

        switch (action) {
            case "crear":
                HttpSession session = request.getSession();
                String rol = (String) session.getAttribute("rol");
                if (rol.equalsIgnoreCase("Top 1") || rol.equalsIgnoreCase("Top 2")) {

                    departmentId = Integer.parseInt(request.getParameter("id"));
                    String departmentName = request.getParameter("departmentName");
                    int managerId = Integer.parseInt(request.getParameter("managerId"));
                    int locationId = Integer.parseInt(request.getParameter("locationId"));

                    department = departmentDao.obtener(departmentId);

                    if (department == null) {
                        departmentDao.crear(departmentId, departmentName, managerId, locationId);
                    } else {
                        departmentDao.actualizar(departmentId, departmentName, managerId, locationId);
                    }
                    response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                }
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        HttpSession session = request.getSession();
        String rol = (String) session.getAttribute("rol");
        DepartmentDao departmentDao = new DepartmentDao();
        RequestDispatcher view;
        Department department;
        int departmentId;

        switch (action) {
            case "formCrear":
                if (rol.equalsIgnoreCase("Top 1") || rol.equalsIgnoreCase("Top 2")) {
                    view = request.getRequestDispatcher("department/newDepartment.jsp");
                    view.forward(request, response);
                }
                break;
            case "lista":
                ArrayList<Department> lista = departmentDao.listar();

                request.setAttribute("lista", lista);

                view = request.getRequestDispatcher("department/listaDepartment.jsp");
                view.forward(request, response);
                break;

            case "editar":
                if (rol.equalsIgnoreCase("Top 1") || rol.equalsIgnoreCase("Top 3")) {
                    departmentId = Integer.parseInt(request.getParameter("id"));
                    department = departmentDao.obtener(departmentId);
                    if (department == null) {
                        response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                    } else {
                        request.setAttribute("department", department);
                        view = request.getRequestDispatcher("department/updateDepartment.jsp");
                        view.forward(request, response);
                    }
                }
                break;
            case "borrar":
                if (rol.equalsIgnoreCase("Top 1") || rol.equalsIgnoreCase("Top 2")) {
                    departmentId = Integer.parseInt(request.getParameter("id"));
                    if (departmentDao.obtener(departmentId) != null) {
                        departmentDao.borrar(departmentId);
                    }
                    response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                }
                break;
        }
    }
}
