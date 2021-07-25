import java.lang.annotation.*;

/**
 * @author chenzufeng
 * @date 2021/07/25
 * @usage FruitName 水果名称注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}
