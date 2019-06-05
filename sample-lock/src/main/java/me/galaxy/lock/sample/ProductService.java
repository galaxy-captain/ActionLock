package me.galaxy.lock.sample;

import me.galaxy.lock.spring.annotation.LockAction;
import me.galaxy.lock.spring.annotation.LockName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    @LockAction
    public int saveProduct(@LockName("productDTO.name[name]||ProductDTO.company[0]") ProductDTO productDTO, int price) {

        Product product = productDAO.findProductByNameCompany(
                productDTO.getName().get("name").toString(),
                productDTO.getCompany().get(0).toString()
        );

        if (product == null) {
            productDAO.saveProduct(
                    productDTO.getName().get("name").toString()
                    , productDTO.getPrice(),
                    productDTO.getCompany().get(0).toString()
            );
            return 1;
        }

        return 0;
    }

}
