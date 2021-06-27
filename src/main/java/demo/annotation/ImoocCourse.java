package demo.annotation;

/**
 * @author malaka
 * @create 2020-12-03 10:37
 */
@CourseInfoAnnotation(courseName = "english writing",
        courseTag = "english", courseProfile = "teach you english writing!")
public class ImoocCourse {


    @PersonInfoAnnotation(name = "ali", language = {"java","C++"})
    private String author;

    @CourseInfoAnnotation(courseName = "poem writing", courseTag = "writing",
            courseProfile = "teach you to write a poem!",
            courseIndex = 143)
    public void getCourseInfo(){

    }
}
