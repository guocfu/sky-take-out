package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
        // 公共字段自动填充
    void insert(Dish dish);


    //SELECT d.id,d.name,d.category_id,d.price,d.image,d.description,d.status,d.update_time,
    // c.name AS categoryName FROM dish d LEFT JOIN category c ON d.category_id=c.id
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据主键查询菜品
     *
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据主键集合查询多个菜品
     *
     * @param ids
     * @return
     */
    List<Dish> getByIds(List<Long> ids);

    /**
     * 根据菜品id集合删除对应的菜品
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 关联菜品表和菜品套餐表，分解套餐id查询相应的菜品信息
     *
     * @param setmealId
     * @return
     */
    @Select("select d.* from dish d left join setmeal_dish s on d.id=s.dish_id where s.setmeal_id=#{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据条件统计菜品数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}

