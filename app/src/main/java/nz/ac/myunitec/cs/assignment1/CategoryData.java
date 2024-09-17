package nz.ac.myunitec.cs.assignment1;

public class CategoryData {
    private String categoryName;
    private Integer categoryImage;

    public CategoryData(String categoryName, Integer categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(Integer categoryImage) {
        this.categoryImage = categoryImage;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
