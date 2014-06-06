package com.cosooki.topcheck.tools;

public class ParseException extends Exception {
    private static final long serialVersionUID = 1L;

    private int _code;
    private String _cause;

    public ParseException(int code, String cause) {
        this._code = code;
        this._cause = cause;
    }

    public int getCode() {
        return _code;
    }

    public String getCause_() {
        return _cause;
    }

    @Override
    public String toString() {
        return String.format("ParseException(%1$d): %2$s", _code, _cause);
    }
}
