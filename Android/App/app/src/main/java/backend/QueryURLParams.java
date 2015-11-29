package backend;

import java.util.HashMap;

public class QueryURLParams {
    String urlText;
    HTTPMethod method;
    HashMap<String, Object> arguments;

    QueryURLParams(String urlText_, HTTPMethod method_, HashMap<String, Object> arguments_) {
        urlText = urlText_;
        method = method_;
        arguments = arguments_;
    }
}
