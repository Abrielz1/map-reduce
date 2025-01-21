package homework.model;

public enum PATHS {

    FILES {
        public String toString() {
            return "src/main/resources/";
        }
    },

    PROCESSED {
        public String toString() {
            return "src/main/resources/processed/";
        }
    },

    RESULT{
       public String toString() {
           return "src/main/resources/result/";
       }
    }
}
