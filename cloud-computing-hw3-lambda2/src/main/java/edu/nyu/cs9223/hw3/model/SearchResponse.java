package edu.nyu.cs9223.hw3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuweiran
 */
@Setter
@Getter
@NoArgsConstructor
public class SearchResponse implements Serializable, Cloneable {
    private boolean success;
    private String errorMessage;
    private List<String> matched;
}
