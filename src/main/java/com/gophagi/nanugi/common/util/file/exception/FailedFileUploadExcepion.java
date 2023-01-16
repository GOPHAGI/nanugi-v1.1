package com.gophagi.nanugi.common.util.file.exception;

import com.gophagi.nanugi.common.excepion.CommonExcepion;
import com.gophagi.nanugi.common.excepion.ErrorCode;

public class FailedFileUploadExcepion extends CommonExcepion {
    public FailedFileUploadExcepion(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
