-- INSERT INTO TB_USER(EMAIL, NAME, PASSWORD, CONTACT, EMPLOYEE_CODE, COMPANY_CODE, DEPARTMENT_CODE, DELETE_YN, PUSH_AGREE_YN, INSERT_DATE, UPDATE_DATE, LOGIN_FAIL_CNT, PASSWORD_MODIFY_DATE) VALUES('it1485@gsitm.com', '박다솜', '$2a$10$FmJYBn/KEoBx1dLkHHtE/ecxztVkQK9MyHIRhYv8c2u665sFmJ72O', '01082510613', 'it1485', 'C0001', 'D0001', 'N', 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP);
INSERT INTO COMPANY(CODE, NAME) VALUES('C0001', 'GS ITM');

INSERT INTO TB_USER(EMAIL, NAME, PASSWORD, CONTACT, DELETE_YN, PUSH_AGREE_YN, INSERT_DATE, UPDATE_DATE, LOGIN_FAIL_CNT, PASSWORD_MODIFIED_DATE, COMPANY_ID) VALUES('it1485@gsitm.com', '박다솜', '$2a$10$FmJYBn/KEoBx1dLkHHtE/ecxztVkQK9MyHIRhYv8c2u665sFmJ72O', '01082510613', 'N', 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 1);

INSERT INTO TB_USER_ROLE (ROLE, USER_ID) VALUES('ROLE_ADMIN', '1');
