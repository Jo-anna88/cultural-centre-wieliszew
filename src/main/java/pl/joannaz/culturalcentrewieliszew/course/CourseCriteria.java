package pl.joannaz.culturalcentrewieliszew.course;

import java.math.BigDecimal;

public class CourseCriteria {
    private int minAge;
    private int maxAge;
    private BigDecimal price;
    private String teacher;
    private Category category;
    private String name;
    private String location; // we can take a city from address field

    public static class Builder {
        private CourseCriteria courseCriteria = new CourseCriteria();

        public Builder setMinAge(int minAge) {
            courseCriteria.minAge = minAge;
            return this;
        }

        public Builder setMaxAge(int maxAge) {
            courseCriteria.maxAge = maxAge;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            courseCriteria.price = price;
            return this;
        }

        public Builder setTeacher(String teacher){
            courseCriteria.teacher = teacher;
            return this;
        }

        public Builder setCategory(Category category) {
            courseCriteria.category = category;
            return this;
        }

        public Builder setName(String name) {
            courseCriteria.name = name;
            return this;
        }

        public Builder setLocation(String location) {
            courseCriteria.location = location;
            return this;
        }

        public CourseCriteria build() {
            return this.courseCriteria;
        }
    }
}
