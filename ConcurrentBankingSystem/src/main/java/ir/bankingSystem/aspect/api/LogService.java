package ir.bankingSystem.aspect.api;


import ir.bankingSystem.model.entity.CompressedExceptionLog;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.SQLException;

public interface LogService {

    CompressedExceptionLog saveCompressedExceptionLog(HttpServletRequest request , String description) throws SQLException;



}
