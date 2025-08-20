package ir.bankingSystem.aspect.imple;

import ir.bankingSystem.model.entity.CompressedExceptionLog;
import ir.bankingSystem.repository.CompressedExceptionLogRepository;
import ir.bankingSystem.aspect.api.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.Date;

@Service
@AllArgsConstructor
public class LogServiceImpl  implements LogService {

    private final CompressedExceptionLogRepository compressedExceptionLogRepository;


    @Override
    @Transactional
    public CompressedExceptionLog saveCompressedExceptionLog(HttpServletRequest request, String description) throws SQLException {

        CompressedExceptionLog log = CompressedExceptionLog.builder()
                .compressedStack(new SerialBlob(description.getBytes()))
                .occurDate(new Date())
                .url(request.getRequestURI())
                .build();

        return compressedExceptionLogRepository.save(log);
    }
}
