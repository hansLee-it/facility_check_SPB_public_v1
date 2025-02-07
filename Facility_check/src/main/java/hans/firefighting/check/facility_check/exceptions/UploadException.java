package hans.firefighting.check.facility_check.exceptions;

import java.io.Serial;

/**
 * <pre>
 * 1. Class Name : UploadException
 * 2. Write Date   : 2024-04-10 오후 7:51
 * 3. Author   : itHans
 * 4. 설명 : Exception when upload Files.
 *
 *
 *
 */
public class UploadException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = -5424272000013729765L;

    public UploadException(String msg) {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    public UploadException(String msg, Throwable cause) {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }
}