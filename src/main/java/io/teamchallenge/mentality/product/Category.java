package io.teamchallenge.mentality.product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

  @Id
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String imageFilename;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category", referencedColumnName = "name")
  private Category parentCategory;

  @Builder.Default
  @OneToMany(mappedBy = "parentCategory")
  private Set<Category> subCategories = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Product> products = new HashSet<>();

  public Category addSubCategory(String categoryName) {
    Category subCategory = new Category();
    subCategory.setName(categoryName);
    this.subCategories.add(subCategory);
    subCategory.setParentCategory(this);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Category category)) {
      return false;
    }
    return Objects.equals(name, category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
