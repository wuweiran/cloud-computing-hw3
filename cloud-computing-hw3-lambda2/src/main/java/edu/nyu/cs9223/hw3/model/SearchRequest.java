package edu.nyu.cs9223.hw3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wuweiran
 */
@Setter
@Getter
@NoArgsConstructor
public class SearchRequest implements Serializable, Cloneable {
    private String q;
}
