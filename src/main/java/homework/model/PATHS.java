package homework.model;

public enum PATHS {

    FILES {
        public String toString() {
            return "src/main/resources/files/";
        }
    },

    PROCESSED {
        public String toString() {
            return "src/main/resources/files/processed/";
        }
    },

    RESULT{
       public String toString() {
           return "src/main/resources/files/result/";
       }
    }
}
