package com.devops.web.test;

import com.devops.common.util.ExcelUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ExcelTest
 * @description: TODO
 * @date 2020/4/18 0:59
 */
public class ExcelTest {

    public static void main(String[] args) {
        String[] names = {"张三", "李四", "王五", "赵六", "刘大"};
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            employee.setId(i + 1);
            employee.setName(names[i]);
            employee.setBirthDate(new Date());
            employee.setPayment(10 + i);
            employee.setBonus(10 + i);
            employeeList.add(employee);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("employees", employeeList);
        try (OutputStream outputStream = new FileOutputStream("D:\\develop\\test-out.xlsx")) {
            ExcelUtils.exportExcel("test.xlsx", params, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Data
    @NoArgsConstructor
    public static class Employee {
        private int id;
        private String name;
        private Date birthDate;
        private double payment;
        private double bonus;
    }
}
