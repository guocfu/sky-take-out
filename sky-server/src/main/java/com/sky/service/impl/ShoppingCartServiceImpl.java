package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 只查询自己的购物车数据
        // DTO没有userId，但是拦截器会解析token得到id并保存
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //判断当前商品是否在购物车中, 最多只可能查到一条数据，返回list是为了接口通用，后面查询购物车也能用
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if(shoppingCartList != null && shoppingCartList.size()>0){
            //如果已经存在，就更新数量，数量加1
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        }else {
            //如果不存在，插入数据，数量就是1

            //判断当前添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                // 口味DTO已经拷贝了
            } else {
                //添加到购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        return shoppingCartMapper.list(ShoppingCart.
                builder().
                userId(BaseContext.getCurrentId()).
                build());
    }

    /**
     * 删除购物车的一个商品
     * @param shoppingCartDTO
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //先查看购物车是否有对应的商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        // 只查询自己的购物车数据
        // DTO没有userId，但是拦截器会解析token得到id并保存
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //判断当前商品是否在购物车中, 最多只可能查到一条数据，返回list是为了接口通用，后面查询购物车也能用
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if(shoppingCartList != null && shoppingCartList.size()>0){
            //如果已经存在，就更新数量，数量加1
            shoppingCart = shoppingCartList.get(0);

            // 如果商品只有一个，删除该商品
            if(shoppingCart.getNumber()==1){
                shoppingCartMapper.deleteById(shoppingCart);

            }else{
                // 否则，修改商品数量
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }

    /**
     * 清空购物车商品
     */
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
