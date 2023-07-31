package model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class CSV {
    @CsvBindByPosition(position = 0)
    private int id;
}
