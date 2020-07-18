import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private final BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

        System.out.println("Enter number from 2 to 13. Enter END to exit.");
        String input = null;
        try {
            input = this.bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!input.equals("END")) {

            switch (input) {

                case "2":
                    //TASK 2
                    this.removeObjectTaskTwo();
                    break;
                case "3":
                    //TASK 3
                    try {
                        this.containsEmployeeTaskThree();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    //TASK 4
                    this.findEmployeesWithSalaryOver50kTaskFour();
                    break;
                case "5":
                    //TASK 5
                    this.findEmployeesFromResearchFive();
                    break;
                case "6":
                    //TASK 6
                    try {
                        this.insertAddressToLastName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "7":
                    //TASK 7
                    this.findAllAddressesByEmployeesSeven();
                    break;
                case "8":
                    //TASK 8
                    try {
                        this.findEmployeeAndTheirProjectsEight();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "9":
                    //TASK 9
                    this.findLastStartedProjectsNine();
                    break;
                case "10":
                    //TASK 10
                    this.updateSalaryOfEmployeesInCertainDepartments();
                    break;
                case "11":
                    //TASK 11
                    try {
                        this.deleteTownEleven();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "12":
                    //TASK 12
                    try {
                        this.findEmployeesByPattern();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "13":
                    //TASK 13
                    this.findMaxSalaryByDept();
                    break;

                default:
                    System.out.println("Incorrect input! Please enter number from 2 to 13 or END (if you want to exit).");
            }

            System.out.println();
            System.out.println("Enter number from 2 to 13. Enter END to exit.");
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTownEleven() throws IOException {
        System.out.println("Task eleven:");
        System.out.println("Enter a town to be deleted");
        String town = this.bufferedReader.readLine();

        List<Town> towns = entityManager.createQuery("select t from Town as t where t.name = :tName", Town.class).setParameter("tName", town).getResultList();
        int townId = towns.get(0).getId();
        List<Address> addresses = entityManager.createQuery("select a from Address as a where a.town.name = :tName", Address.class).setParameter("tName", town).getResultList();

        System.out.printf("%d addresses in %s deleted%n", addresses.size(), town);

        entityManager.getTransaction().begin();
        Query queryFirstF = entityManager.createNativeQuery("update employees " +
                "join addresses as a on a.address_id = employees.address_id " +
                "join towns as t on t.town_id = a.town_id " +
                "set employees.address_id = null where t.town_id = " + townId);
        queryFirstF.executeUpdate();
        Query queryFirst = entityManager.createNativeQuery("DELETE FROM addresses WHERE town_id = " + townId);
        queryFirst.executeUpdate();
        Query query = entityManager.createNativeQuery("DELETE FROM towns WHERE name = '" + town + "'");
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    private void findEmployeesByPattern() throws IOException {
        System.out.println("Task twelve:");
        System.out.println("Enter a pattern by which to find the first name on an employee");
        String pattern = this.bufferedReader.readLine();

        Query query = entityManager.createQuery("SELECT e FROM Employee as e WHERE e.firstName LIKE :eValue");

        query.setParameter("eValue", pattern + "%");

        List<Employee> employee = query.getResultList();
        employee.forEach(employee1 ->
                System.out.printf("%s %s - %s - ($%.2f)%n", employee1.getFirstName(), employee1.getLastName(),
                        employee1.getJobTitle(), employee1.getSalary()));
    }

    private void findMaxSalaryByDept() {
        System.out.println("Task thirteen:");
        List<Department> departments = entityManager.createQuery("select d from Department as d", Department.class).getResultList();

        for (Department department : departments) {
            double salary = 0;

            for (Employee employee : department.getEmployees()) {
                if (employee.getSalary().doubleValue() > salary) {
                    salary = employee.getSalary().doubleValue();
                }
            }
            if (!(salary >= 30000) || !(salary <= 70000)) {
                System.out.printf("%s %.2f%n", department.getName(), salary);
            }
        }
    }

    private void insertAddressToLastName() throws IOException {
        System.out.println("Task six:");
        System.out.println("Enter the last name of the employee who will live on Vitoshka 15.");
        String employeeName = this.bufferedReader.readLine();

        entityManager.getTransaction().begin();
        Address address = new Address();
        address.setText("Vitoshka 15");
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        List<Employee> employees = entityManager.createQuery("select e from Employee as e where e.lastName = :name", Employee.class)
                .setParameter("name", employeeName).getResultList();

        entityManager.getTransaction().begin();
        for (Employee employee : employees) {
            entityManager.detach(employee);
            employee.setAddress(address);
            entityManager.merge(employee);
        }
        entityManager.getTransaction().commit();

        System.out.println("Task six: completed.");
    }

    private void updateSalaryOfEmployeesInCertainDepartments() {
        System.out.println("Task ten:");
        TypedQuery<Employee> emQuery =
                entityManager.createQuery("select e from Employee as e where e.department.name = 'Engineering' or e.department.name = 'Tool Design' or " +
                                "e.department.name = 'Marketing' or e.department.name = 'Information Services'",
                        Employee.class);
        List<Employee> allEm = emQuery.getResultList();
        for (Employee em : allEm) {
            double currentSalary = em.getSalary().doubleValue() * 1.12;
            em.setSalary(BigDecimal.valueOf(currentSalary));
        }

        List<Employee> employees = this.entityManager
                .createQuery("select e from Employee as e where e.department.name = 'Engineering' or e.department.name = 'Tool Design' or " +
                                "e.department.name = 'Marketing' or e.department.name = 'Information Services'",
                        Employee.class)
                .getResultList();
        employees.forEach(employee -> System.out.printf("%s %s ($%.2f)%n"
                , employee.getFirstName(), employee.getLastName(), employee.getSalary()));
    }

    private void findLastStartedProjectsNine() {
        System.out.println("Task nine:");
        List<Project> projects = this.entityManager
                .createQuery("select p from Project as p order by p.startDate desc", Project.class)
                .setMaxResults(10)
                .getResultList()
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .collect(Collectors.toList());
        projects.forEach(project -> System.out.printf("Project name: %s%n" +
                        "       Project Description: %s%n" +
                        "       Project Start Date: %s%n" +
                        "       Project End Date: %s%n"
                , project.getName(), project.getDescription(), project.getStartDate()
                , project.getEndDate()));
    }

    private void findEmployeeAndTheirProjectsEight() throws IOException {
        System.out.println("Task eight:");
        System.out.println("Enter the ID of the employee.");
        int employeeId = Integer.parseInt(this.bufferedReader.readLine());

        try {
            Employee employee = this.entityManager
                    .createQuery("select e from Employee as e " +
                            "where e.id = :idE", Employee.class)
                    .setParameter("idE", employeeId)
                    .getSingleResult();

            System.out.printf("%s %s - %s%n"
                    , employee.getFirstName(), employee.getLastName()
                    , employee.getJobTitle());
            Set<Project> employeeProjects = employee.getProjects();
            List<String> projectsNames = new ArrayList<>();

            for (Project project : employeeProjects) {
                projectsNames.add(project.getName());
            }

            Collections.sort(projectsNames);

            projectsNames.
                    forEach(project -> System.out.println("     " + project));
        } catch (NoResultException noResult) {
            System.out.println("Wrong id!");
        }
    }

    private void findAllAddressesByEmployeesSeven() {
        System.out.println("Task seven:");
        List<Address> addresses = this.entityManager
                .createQuery("select a from Address as a left join Employee as e on a.id = e.address.id " +
                        "group by a.text order by count(e.id) desc", Address.class)
                .setMaxResults(10)
                .getResultList();
        addresses.forEach(address -> System.out.printf("%s, %s - %d employees%n"
                , address.getText(), address.getTown().getName()
                , address.getEmployees().size()));
    }

    private void findEmployeesFromResearchFive() {
        System.out.println("Task five:");
        List<Employee> employees = this.entityManager
                .createQuery("select e from Employee as e where e.department.id = 6 order by e.salary asc, e.id asc", Employee.class)
                .getResultList();
        employees.forEach(employee -> System.out.printf("%s %s from Research and Development - $%.2f%n"
                , employee.getFirstName(), employee.getLastName(), employee.getSalary()));
    }

    private void findEmployeesWithSalaryOver50kTaskFour() {
        System.out.println("Task four:");
        List<Employee> employees = this.entityManager
                .createQuery("select e from Employee as e where e.salary > 50000", Employee.class)
                .getResultList();
        employees.forEach(employee -> System.out.println(employee.getFirstName()));
    }

    private void containsEmployeeTaskThree() throws IOException {
        System.out.println("Task three:");
        System.out.println("Enter the full name of the employee.");
        String employeeName = this.bufferedReader.readLine();

        try {
            Employee employee = this.entityManager
                    .createQuery("select e from Employee as e " +
                            "where concat(e.firstName, ' ', e.lastName) = :name", Employee.class)
                    .setParameter("name", employeeName)
                    .getSingleResult();

            System.out.println("Yes");
        } catch (NoResultException noResult) {
            System.out.println("No");
        }
    }

    private void removeObjectTaskTwo() {
        List<Town> towns = this.entityManager
                .createQuery("select t from Town as t where length(t.name) > 5", Town.class)
                .getResultList();

        this.entityManager.getTransaction().begin();
        towns.forEach(this.entityManager::detach);

        this.entityManager.createQuery("update Town as t set t.name = lower(t.name) where length(t.name) <=5")
                .executeUpdate();

        this.entityManager.flush();
        this.entityManager.getTransaction().commit();

        System.out.println("Task two: completed.");
    }
}
