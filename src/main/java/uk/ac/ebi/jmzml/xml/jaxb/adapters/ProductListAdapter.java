package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import uk.ac.ebi.jmzml.model.mzml.Product;
import uk.ac.ebi.jmzml.model.mzml.ProductList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: melih
 * Date: 25/02/2011
 * Time: 09:32
 * To change this template use File | Settings | File Templates.
 */
public class ProductListAdapter extends XmlAdapter<ProductList, List<Product>> {
    @Override
    public List<Product> unmarshal(ProductList productList) throws Exception {
        return productList.getProduct();
    }

    @Override
    public ProductList marshal(List<Product> products) throws Exception {
        ProductList productList = new ProductList();
        productList.getProduct().addAll(products);
        productList.setCount(new BigInteger(String.valueOf(products.size())));
        return productList;
    }
}
