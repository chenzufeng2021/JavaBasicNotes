import java.lang.annotation.*;

/**
 * @author chenzufeng
 * @date 2021/7/25
 * @usage FruitColor 水果颜色注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {
    /**
     * 颜色枚举
     */
    public enum Color {BLUE, RED, GREEN};

    /**
     * 颜色属性
     * @return 颜色属性，默认为绿色
     */
    Color fruitColor() default Color.GREEN;
}
