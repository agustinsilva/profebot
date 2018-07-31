package ar.com.profebot.ia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IAModuleParams {

    @JsonProperty("root")
    private String root;

    @JsonProperty("term")
    private String term;

    @JsonProperty("context")
    private String context;

    @JsonCreator
    public IAModuleParams(@JsonProperty("root") String root,
                          @JsonProperty("term") String term,
                          @JsonProperty("context") String context) {
        this.root = root;
        this.term = term;
        this.context = context;
    }

    @JsonProperty("root")
    public String getRoot() {
        return root;
    }

    @JsonProperty("term")
    public String getTerm() {
        return term;
    }

    @JsonProperty("context")
    public String getContext() {
        return context;
    }
}
