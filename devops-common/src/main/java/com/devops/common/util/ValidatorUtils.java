package com.devops.common.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yangge
 * @version 1.0.0
 * @title: ValidatorUtils
 * @description: TODO
 * @date 2020/4/18 23:55
 */
public class ValidatorUtils {

    public static Validator failFastValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory().getValidator();

    public static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(false)
            .buildValidatorFactory().getValidator();


    public static <T> List<String> validateBean(boolean failFast, T data) {

        Validator validator = failFast ? failFastValidator : ValidatorUtils.validator;
        Set<ConstraintViolation<T>> violation = validator.validate(data);
        if (!CollectionUtils.isEmpty(violation)) {
            List<String> errors = new ArrayList<>();
            violation.forEach(error -> {
                errors.add(error.getMessage());
            });
            return errors;
        }

        return null;
    }

    public static void main(String[] args) {
        String[] names = {"张三", "李四", "王五", "赵六", "刘大"};
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            employee.setId(i + 1 + "21212111212121212112");
            employee.setName(names[i]);
            employee.setBirthDate("1992-101");
            employee.setPayment(10 + i + "c");
            employeeList.add(employee);
        }
        List<List<String>> errors = new ArrayList<>();
        for (Employee employee : employeeList) {
            List<String> strings = validateBean(true, employee);
            errors.add(strings);
        }
        System.out.println(errors);
    }

    @Data
    @NoArgsConstructor
    public static class Employee {

        @Pattern(regexp = "^[0-9]*$", message = "员工ID必须为数字")
        @Size(min = 1, max = 10, message = "id超长")
        @NotNull(message = "员工ID不能为空")
        private String id;

        @Length(min = 2, max = 10, message = "姓名长度不符合要求，2-10之间")
        private String name;

        //@Past(message = "出生日期不能大于当前日期")
        @Pattern(regexp = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "出生日期格式不正确")
        private String birthDate;

        @NotNull(message = "支付金额不能为空")
        @Digits(integer = 6, fraction = 2, message = "支付金额格式不正确")
        private String payment;
    }
}
