package me.serebryakov.weatherbot.POJO;

import lombok.Data;

/**
 * Класс, представляющий условия погоды.
 */
@Data
public class Condition {

    /**
     * Текстовое описание погоды.
     */
    private String text;

    /**
     * URL иконки, отображающей состояние погоды.
     */
    private String icon;

    /**
     * Код состояния погоды.
     */
    private int code;
}