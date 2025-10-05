package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal where id=#{setmealId}")
    Setmeal getById(Long setmealId);

    @Delete("delete from setmeal where id=#{setmealId}")
    void deleteById(Long setmealId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 关联套餐表和套餐菜品表，根据菜品id查询套餐信息（用于判断菜品对应的套餐是否售卖中）
     * @param dishId
     * @return
     */
    @Select("select s.* from setmeal s left join setmeal_dish sd on s.id=sd.setmeal_id where sd.dish_id=#{dishId}")
    List<Setmeal> getSetmealByDishId(Long dishId);
}
