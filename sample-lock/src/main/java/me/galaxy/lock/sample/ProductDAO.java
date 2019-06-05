package me.galaxy.lock.sample;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public Product findProductByNameCompany(String name, String company) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("company", company);

        return sqlSessionTemplate.selectOne("me.galaxy.lock.sample.ProductMapper.findProductByNameCompany", parameters);
    }

    public void saveProduct(String name, int price, String company) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("price", price);
        parameters.put("company", company);

        sqlSessionTemplate.insert("me.galaxy.lock.sample.ProductMapper.saveProduct", parameters);
    }

}
