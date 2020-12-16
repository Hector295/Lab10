package Daos;

import Beans.Department;
import Dtos.SalarioPorDepartamentoDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DTI
 */
public class DepartmentDao extends DaoBase {

    public ArrayList<Department> listar() {

        ArrayList<Department> lista = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departments")) {

            while (rs.next()) {
                Department department = new Department();
                department.setDepartmentId(rs.getInt(1));
                department.setDepartmentName(rs.getString(2));
                department.setManagerId(rs.getInt(3));
                department.setLocationId(rs.getInt(4));
                lista.add(department);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public void crear(int departmentId, String departmentName, int managerId, int locationId) {
        try {
            try (Connection conn = this.getConection();) {
                String sql = "INSERT INTO departments (`department_id`, `department_name`, `manager_id`, `location_id`) "
                        + "VALUES (?,?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, departmentId);
                    pstmt.setString(2, departmentName);
                    pstmt.setInt(3, managerId);
                    pstmt.setInt(4, locationId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Department obtener(int departmentId) {

        Department department = null;
        try {
            String sql = "SELECT * FROM departments WHERE department_id = ?";
            try (Connection conn = this.getConection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
                pstmt.setInt(1, departmentId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        department = new Department();
                        department.setDepartmentId(rs.getInt(1));
                        department.setDepartmentName(rs.getString(2));
                        department.setManagerId(rs.getInt(3));
                        department.setLocationId(rs.getInt(4));
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return department;
    }

    public void actualizar(int departmentId, String departmentName, int managerId, int locationId) {
        try {
            try (Connection conn = this.getConection();) {
                String sql = "UPDATE departments SET department_name = ?, manager_id = ?, location_id = ? "
                        + "WHERE department_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, departmentName);
                    pstmt.setInt(2, managerId);
                    pstmt.setInt(3, locationId);
                    pstmt.setInt(4, departmentId);
                    pstmt.executeUpdate();
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void borrar(int departmentId) {
        try {
            try (Connection conn = this.getConection();) {
                String sql = "DELETE FROM departments WHERE department_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, departmentId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DepartmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<SalarioPorDepartamentoDto> listaSalarioPorDepartamento() {

        ArrayList<SalarioPorDepartamentoDto> lista = new ArrayList<>();
        String sql = "select department_name, min(salary), max(salary), truncate (avg (salary),2)"
                + "from departments d "
                + "inner join employees e on e.department_id = d.department_id "
                + "group by d.department_name order by d.department_name";

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SalarioPorDepartamentoDto dto = new SalarioPorDepartamentoDto();
                dto.setNombreDepartamento(rs.getString(1));
                dto.setSalarioMinimo(rs.getBigDecimal(2));
                dto.setSalarioMaximo(rs.getBigDecimal(3));
                dto.setSalarioPromedio(rs.getBigDecimal(4));
                lista.add(dto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public boolean isJefeDepartamento(int employeeId) {

        boolean retorno = false;


        return retorno;
    }

}
