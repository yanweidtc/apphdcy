package com.hdcy.app.model;

import java.io.Serializable;

/**
 * 资讯分类实体类
 * Created by WeiYanGeorge on 2016-08-15.
 */

public class NewsCategory implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

/*    参数	属性	说明
    id	int	分类ID
    Name	Stirng	分类名称*/

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
