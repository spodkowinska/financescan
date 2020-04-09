package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.transaction.TransactionRepository;
import info.podkowinski.sandra.financescanner.project.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final ProjectRepository projectRepository;

    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository,
                           ProjectRepository projectRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.projectRepository = projectRepository;
    }


    public List<Category>findByProjectId(Long id){
        return categoryRepository.findAllByProjectId(id);
    }



    public void save(Category category){ categoryRepository.save(category); }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

//    Category compareCategories(Long categoryId, Category newCategory){
//        Category category = categoryRepository.findById(categoryId).orElse(null);
//        if (!category.equals(newCategory)){
//            category.name=newCategory.name;
//            category.description=newCategory.description;
//            category.keywords=filterKeywords(newCategory.keywords);
//            category.parentCategoryId=newCategory.parentCategoryId;
//        }
//        return category;
//    }
    void delete(Category category) {

        categoryRepository.delete(category);
    }

    public List<String> areValidKeywords(String [] keywordToCheck) {
        List<String> keywordsList = new ArrayList<>();
        for (String word : keywordToCheck) {
            if (word != null && word.length() != 0 && word != " ") {
                keywordsList.add(word.trim());
            }
        }
        return keywordsList;
    }

    Long findNumberOfTransactionsPerCategory(Long categoryId){
      return  categoryRepository.findNumberOfTransactionsPerCategory(categoryId);
    }

    Long findNumberOfTransactionsPerPendingCategory(Long categoryId){
        return  categoryRepository.findNumberOfTransactionsPerPendingCategory(categoryId);
    }
//    public Long doesKeywordExistInDifferentCategory(String keyword, Long categoryId){
//        List<Category> projectsCategories = categoryRepository.findAllByProjectId(2l);
//        for (Category category :projectsCategories) {
//            if (!category.equals(categoryRepository.findById(categoryId))) {
//                for (String key : category.keywords) {
//                    if (key.toLowerCase().equals(keyword.toLowerCase())) {
//                        return category.getId();
//                    }
//                }
//            }
//        }
//        return -1l;
//    }

    void createDefaultCategory_Dzieci (Long projectId) {
        Category category = new Category();
        category.name = "Dzieci";
        category.description = "Art. szkolne i książki, Kieszonkowe, Kursy i zajęcia dodatkowe, Odzież i buty, Opieka, Zabawki itp";
        category.fontColor = "#ffffff";
        category.color = "#ff2f92";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Zdrowie (Long projectId) {
        Category category = new Category();
        category.name = "Zdrowie";
        category.description = "Badania, Wizyty u lekarzy, Okulary, Ubezpieczenie zdrowotne, Lekarstwa i witaminy";
        category.fontColor = "#ffffff";
        category.color = "#008f00";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Transport (Long projectId) {
        Category category = new Category();
        category.name = "Transport";
        category.description = "Paliwo, Ubezpieczenie auta, Przeglądy i myjnia, autostrady, Bilety komunikacji miejskiej";
        category.fontColor = "#000000";
        category.color = "#919191";
        category.project = projectRepository.findById(projectId).orElse(null);
    }
    void createDefaultCategory_Zyciecodzienne (Long projectId) {
        Category category = new Category();
        category.name = "Życie Codzienne";
        category.description = "Żywność, Higiena, Środki czystości, Kosmetyki";
        category.fontColor = "#000000";
        category.color = "#f0c0bc";
        category.project = projectRepository.findById(projectId).orElse(null);
    }
    void createDefaultCategory_Inwetycje (Long projectId) {
        Category category = new Category();
        category.name = "Inwestycje";
        category.description = "Budowa domu, Zakup akcji/obligacji/funduszy, Zakup działki, Zakup mieszkania";
        category.fontColor = "#ffffff";
        category.color = "#011993";
        category.project = projectRepository.findById(projectId).orElse(null);
    }
    void createDefaultCategory_Rozwoj (Long projectId) {
        Category category = new Category();
        category.name = "Rozwój";
        category.description = "Czesne, Kursy i szkolenia, Nauka języków, Pomoce naukowe";
        category.fontColor = "#ffffff";
        category.color = "#9437ff";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_RozrywkaiHobby (Long projectId) {
        Category category = new Category();
        category.name = "Rozrywka & Hobby";
        category.description = "Kino, Teatr, Koncerty, Komputer i akcesoria, Książki i prasa, Restauracje, Akcesoria sportowe, Basen, Fitness, SPA";
        category.fontColor = "#000000";
        category.color = "#f7f91e";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Mieszkanie (Long projectId) {
        Category category = new Category();
        category.name = "Mieszkanie";
        category.description = "Kredyt hipoteczny, Czynsz, Opłaty i podatki, Akcesoria domowe, Meble, Ogród, Remonty i naprawy, Sprzęty RTV i AGD";
        category.fontColor = "#000000";
        category.color = "#00fa92";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Abonamenty (Long projectId) {
        Category category = new Category();
        category.name = "Abonamenty";
        category.description = "Internet, Telefony, Netflix, Spotify, Dysk Google, Inne abonamenty";
        category.fontColor = "#ffffff";
        category.color = "#9437ff";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Rekreacja (Long projectId) {
        Category category = new Category();
        category.name = "Rekreacja";
        category.description = "Wyjazdy wakacyjne, Wyjazdy weekendowe";
        category.fontColor = "#ffffff";
        category.color = "#ec1b00";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Odzież (Long projectId) {
        Category category = new Category();
        category.name = "Odzież";
        category.description = "Akcesoria galanteryjne, Biżuteria, Buty, Ubrania";
        category.fontColor = "#000000";
        category.color = "#ef9f32";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Prezenty (Long projectId) {
        Category category = new Category();
        category.name = "Prezenty";
        category.description = "Działalność charytatywna, Działalność społeczna, Prezenty";
        category.fontColor = "#000000";
        category.color = "#33dfeb";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Inne (Long projectId) {
        Category category = new Category();
        category.name = "Inne";
        category.description = "Wydatki nieregularne, kary finansowe, itp";
        category.fontColor = "#ffffff";
        category.color = "#2e150c";
        category.project = projectRepository.findById(projectId).orElse(null);
    }

    void createDefaultCategory_Wpływy (Long projectId) {
        Category category = new Category();
        category.name = "Wpływy";
        category.description = "Wpływy na konto, pensje, dodatkowe dochody itp";
        category.fontColor = "#000000";
        category.color = "#04fb28";
        category.project = projectRepository.findById(projectId).orElse(null);
    }
}