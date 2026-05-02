<template>
  <div class="add-book-form">
    <h2 class="form-title">Добавить книгу</h2>
    
    <form @submit.prevent="submitForm">
      <!-- Основная информация -->
      <div class="form-section">
        <h3 class="section-title">Основная информация</h3>
        
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label class="form-label">Название книги *</label>
              <input 
                v-model="formData.title" 
                type="text" 
                class="form-control"
                required
              >
            </div>
          </div>
          
          <div class="col-md-6">
            <div class="form-group">
              <label class="form-label">Автор *</label>
              <select 
                v-model="formData.authorId" 
                class="form-select"
                required
              >
                <option value="">Выберите автора</option>
                <option 
                  v-for="author in authors" 
                  :key="author.id" 
                  :value="author.id"
                >
                  {{ author.lastName }} {{ author.firstName }} {{ author.middleName }}
                </option>
              </select>
            </div>
          </div>
        </div>
        
        <div class="row">
          <div class="col-md-4">
            <div class="form-group">
              <label class="form-label">Год публикации *</label>
              <input 
                v-model="formData.publicationYear" 
                type="number" 
                class="form-control"
                min="1000"
                max="2030"
                required
              >
            </div>
          </div>
          
          <div class="col-md-8">
            <div class="form-group">
              <label class="form-label">URL обложки</label>
              <input 
                v-model="formData.coverUrl" 
                type="url" 
                class="form-control"
                placeholder="https://example.com/cover.jpg"
              >
            </div>
          </div>
        </div>
        
        <div class="form-group">
          <label class="form-label">Описание *</label>
          <textarea 
            v-model="formData.description" 
            class="form-control" 
            rows="3"
            required
          ></textarea>
        </div>
      </div>
      
      <!-- Жанры и теги -->
      <div class="form-section">
        <h3 class="section-title">Классификация</h3>
        
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label class="form-label">Жанры *</label>
              <div class="checkbox-group">
                <div 
                  v-for="genre in genres" 
                  :key="genre.id" 
                  class="form-check"
                >
                  <input 
                    type="checkbox" 
                    :id="'genre-' + genre.id"
                    :value="genre.id"
                    v-model="formData.genreIds"
                    class="form-check-input"
                  >
                  <label 
                    :for="'genre-' + genre.id" 
                    class="form-check-label"
                  >
                    {{ genre.name }}
                  </label>
                </div>
              </div>
            </div>
          </div>
          
          <div class="col-md-6">
            <div class="form-group">
              <label class="form-label">Теги *</label>
              <div class="checkbox-group">
                <!-- Классы -->
                <div class="tag-group">
                  <h4 class="tag-group-title">Класс</h4>
                  <div class="tag-options">
                    <div 
                      v-for="tag in tags.filter(t => t.type === 'GRADE')" 
                      :key="tag.id" 
                      class="form-check"
                    >
                      <input 
                        type="radio" 
                        :id="'tag-' + tag.id"
                        :value="tag.id"
                        v-model="formData.gradeTagId"
                        name="grade"
                        class="form-check-input"
                        required
                      >
                      <label 
                        :for="'tag-' + tag.id" 
                        class="form-check-label"
                      >
                        {{ tag.name }}
                      </label>
                    </div>
                  </div>
                </div>
                
                <!-- Уровни -->
                <div class="tag-group">
                  <h4 class="tag-group-title">Уровень</h4>
                  <div class="tag-options">
                    <div 
                      v-for="tag in tags.filter(t => t.type === 'LEVEL')" 
                      :key="tag.id" 
                      class="form-check"
                    >
                      <input 
                        type="radio" 
                        :id="'tag-' + tag.id"
                        :value="tag.id"
                        v-model="formData.levelTagId"
                        name="level"
                        class="form-check-input"
                        required
                      >
                      <label 
                        :for="'tag-' + tag.id" 
                        class="form-check-label"
                      >
                        {{ tag.name }}
                      </label>
                    </div>
                  </div>
                </div>
                
                <!-- Категории -->
                <div class="tag-group">
                  <h4 class="tag-group-title">Категория</h4>
                  <div class="tag-options">
                    <div 
                      v-for="tag in tags.filter(t => t.type === 'CATEGORY')" 
                      :key="tag.id" 
                      class="form-check"
                    >
                      <input 
                        type="radio" 
                        :id="'tag-' + tag.id"
                        :value="tag.id"
                        v-model="formData.categoryTagId"
                        name="category"
                        class="form-check-input"
                        required
                      >
                      <label 
                        :for="'tag-' + tag.id" 
                        class="form-check-label"
                      >
                        {{ tag.name }}
                      </label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Содержимое -->
      <div class="form-section">
        <h3 class="section-title">Содержимое книги</h3>
        
        <div class="form-group">
          <label class="form-label">Тип хранения *</label>
          <div class="storage-options">
            <div class="form-check">
              <input 
                type="radio" 
                id="storage-text"
                value="TEXT"
                v-model="formData.storageType"
                class="form-check-input"
                required
              >
              <label for="storage-text" class="form-check-label">
                Текст в базе (до 50KB)
              </label>
            </div>
            
            <div class="form-check">
              <input 
                type="radio" 
                id="storage-file"
                value="FILE"
                v-model="formData.storageType"
                class="form-check-input"
              >
              <label for="storage-file" class="form-check-label">
                Файл на сервере
              </label>
            </div>
          </div>
        </div>
        
        <div v-if="formData.storageType === 'TEXT'" class="form-group">
          <label class="form-label">Текст книги *</label>
          <textarea 
            v-model="formData.content" 
            class="form-control" 
            rows="10"
            placeholder="Введите текст книги..."
            required
          ></textarea>
          <div class="form-text">
            Ограничение: 50 000 символов. Сейчас: {{ formData.content?.length || 0 }}
          </div>
        </div>
        
        <div v-else-if="formData.storageType === 'FILE'" class="form-group">
          <label class="form-label">Файлы книги</label>
          
          <!-- EPUB -->
          <div class="file-upload-group">
            <label class="file-label">EPUB файл</label>
            <input 
              type="file" 
              @change="handleFileUpload($event, 'epub')"
              accept=".epub"
              class="form-control"
            >
            <div v-if="uploadedFiles.epub" class="file-info">
              📄 {{ uploadedFiles.epub.name }} ({{ formatFileSize(uploadedFiles.epub.size) }})
            </div>
          </div>
          
          <!-- PDF -->
          <div class="file-upload-group">
            <label class="file-label">PDF файл</label>
            <input 
              type="file" 
              @change="handleFileUpload($event, 'pdf')"
              accept=".pdf"
              class="form-control"
            >
            <div v-if="uploadedFiles.pdf" class="file-info">
              📄 {{ uploadedFiles.pdf.name }} ({{ formatFileSize(uploadedFiles.pdf.size) }})
            </div>
          </div>
          
          <!-- FB2 -->
          <div class="file-upload-group">
            <label class="file-label">FB2 файл</label>
            <input 
              type="file" 
              @change="handleFileUpload($event, 'fb2')"
              accept=".fb2"
              class="form-control"
            >
            <div v-if="uploadedFiles.fb2" class="file-info">
              📄 {{ uploadedFiles.fb2.name }} ({{ formatFileSize(uploadedFiles.fb2.size) }})
            </div>
          </div>
          
          <!-- TXT -->
          <div class="file-upload-group">
            <label class="file-label">TXT файл</label>
            <input 
              type="file" 
              @change="handleFileUpload($event, 'txt')"
              accept=".txt"
              class="form-control"
            >
            <div v-if="uploadedFiles.txt" class="file-info">
              📄 {{ uploadedFiles.txt.name }} ({{ formatFileSize(uploadedFiles.txt.size) }})
            </div>
          </div>
        </div>
      </div>
      
      <!-- Кнопки отправки -->
      <div class="form-actions">
        <button 
          type="button" 
          class="btn btn-secondary"
          @click="$emit('cancel')"
        >
          Отмена
        </button>
        
        <button 
          type="submit" 
          class="btn btn-primary"
          :disabled="loading"
        >
          <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
          {{ loading ? 'Добавляем...' : 'Добавить книгу' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { bookService } from '../services/books.js'
import { authorService } from '../services/authors.js'
import { genreService } from '../services/genres.js'
import { tagService } from '../services/tags.js'

export default {
  name: 'AddBookForm',
  emits: ['success', 'cancel'],
  setup(props, { emit }) {
    const loading = ref(false)
    const authors = ref([])
    const genres = ref([])
    const tags = ref([])
    
    const formData = ref({
      title: '',
      publicationYear: new Date().getFullYear(),
      description: '',
      authorId: '',
      storageType: 'TEXT',
      content: '',
      coverUrl: '',
      genreIds: [],
      tagIds: []
    })
    
    const uploadedFiles = ref({
      epub: null,
      pdf: null,
      fb2: null,
      txt: null
    })
    
    // Загружаем данные для выпадающих списков
    const loadFormData = async () => {
      try {
        const [authorsData, genresData, tagsData] = await Promise.all([
          authorService.getAllAuthors(),
          genreService.getAllGenres(),
          tagService.getAllTags()
        ])
        
        authors.value = authorsData
        genres.value = genresData
        tags.value = tagsData
      } catch (error) {
        console.error('Ошибка загрузки данных формы:', error)
        alert('Не удалось загрузить данные для формы')
      }
    }
    
    // Обработка загрузки файлов
    const handleFileUpload = (event, fileType) => {
      const file = event.target.files[0]
      if (file) {
        uploadedFiles.value[fileType] = file
        
        // Автоматически устанавливаем storageType если файл загружен
        if (formData.value.storageType !== 'FILE') {
          formData.value.storageType = 'FILE'
        }
      }
    }
    
    // Форматирование размера файла
    const formatFileSize = (bytes) => {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }
    
    // Вычисляем tagIds из выбранных тегов
    const computedTagIds = computed(() => {
      const tagIds = []
      
      // Добавляем выбранные теги
      if (formData.value.gradeTagId) tagIds.push(formData.value.gradeTagId)
      if (formData.value.levelTagId) tagIds.push(formData.value.levelTagId)
      if (formData.value.categoryTagId) tagIds.push(formData.value.categoryTagId)
      
      return tagIds
    })
    
    // Отправка формы
    const submitForm = async () => {
      // Валидация
      if (formData.value.genreIds.length === 0) {
        alert('Выберите хотя бы один жанр')
        return
      }
      
      if (computedTagIds.value.length < 3) {
        alert('Выберите тег для класса, уровня и категории')
        return
      }
      
      // Подготовка данных
      const bookData = {
        ...formData.value,
        tagIds: computedTagIds.value
      }
      
      // Если storageType = TEXT, проверяем content
      if (bookData.storageType === 'TEXT' && (!bookData.content || bookData.content.length > 50000)) {
        alert('Для типа TEXT необходимо указать содержимое книги (не более 50000 символов)')
        return
      }
      
      // Если storageType = FILE, проверяем что есть хотя бы один файл
      if (bookData.storageType === 'FILE') {
        const hasFiles = Object.values(uploadedFiles.value).some(file => file !== null)
        if (!hasFiles) {
          alert('Для типа FILE необходимо загрузить хотя бы один файл')
          return
        }
      }
      
      loading.value = true
      
      try {
        // TODO: Сначала загрузить файлы на сервер, получить пути
        // Пока отправляем без файлов
        const createdBook = await bookService.createBook(bookData)
        
        // TODO: После успешного создания книги, загружаем файлы
        // if (bookData.storageType === 'FILE') {
        //   await uploadBookFiles(createdBook.id)
        // }
        
        emit('success', createdBook)
        alert('Книга успешно добавлена!')
        
        // Сбрасываем форму
        resetForm()
        
      } catch (error) {
        console.error('Ошибка добавления книги:', error)
        alert(`Ошибка: ${error.message}`)
      } finally {
        loading.value = false
      }
    }
    
    // Загрузка файлов на сервер
    const uploadBookFiles = async (bookId) => {
      const formData = new FormData()
      formData.append('bookId', bookId)
      
      Object.entries(uploadedFiles.value).forEach(([type, file]) => {
        if (file) {
          formData.append('files', file)
          formData.append('fileTypes', type)
        }
      })
      
      // TODO: Эндпоинт для загрузки файлов
      // await fetch(`/api/books/${bookId}/files`, {
      //   method: 'POST',
      //   body: formData
      // })
    }
    
    // Сброс формы
    const resetForm = () => {
      formData.value = {
        title: '',
        publicationYear: new Date().getFullYear(),
        description: '',
        authorId: '',
        storageType: 'TEXT',
        content: '',
        coverUrl: '',
        genreIds: [],
        tagIds: []
      }
      
      uploadedFiles.value = {
        epub: null,
        pdf: null,
        fb2: null,
        txt: null
      }
    }
    
    onMounted(() => {
      loadFormData()
    })
    
    return {
      loading,
      authors,
      genres,
      tags,
      formData,
      uploadedFiles,
      handleFileUpload,
      formatFileSize,
      computedTagIds,
      submitForm
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap');

.add-book-form {
  font-family: 'Manrope', sans-serif;
  background: #FFFFFF;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  max-width: 900px;
  margin: 0 auto;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: #1B1B1B;
  margin-bottom: 32px;
  text-align: center;
}

.form-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #F0F0F0;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #1B1B1B;
  margin-bottom: 20px;
}

.form-label {
  font-size: 14px;
  font-weight: 600;
  color: #1B1B1B;
  margin-bottom: 8px;
  display: block;
}

.form-control, .form-select {
  font-family: 'Manrope', sans-serif;
  font-size: 14px;
  color: #1B1B1B;
  border: 2px solid #E0E0E0;
  border-radius: 10px;
  padding: 12px 16px;
  transition: all 0.3s;
}

.form-control:focus, .form-select:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.checkbox-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  max-height: 200px;
  overflow-y: auto;
  padding: 12px;
  border: 1px solid #F0F0F0;
  border-radius: 10px;
}

.form-check {
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-check-input {
  width: 18px;
  height: 18px;
  border: 2px solid #E0E0E0;
  border-radius: 4px;
}

.form-check-input:checked {
  background-color: #667eea;
  border-color: #667eea;
}

.form-check-label {
  font-size: 14px;
  color: #1B1B1B;
  cursor: pointer;
}

.tag-group {
  margin-bottom: 20px;
}

.tag-group-title {
  font-size: 14px;
  font-weight: 600;
  color: #1B1B1B;
  margin-bottom: 12px;
}

.tag-options {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.storage-options {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
}

.file-upload-group {
  margin-bottom: 16px;
}

.file-label {
  font-size: 14px;
  font-weight: 600;
  color: #1B1B1B;
  margin-bottom: 8px;
  display: block;
}

.file-info {
  font-size: 12px;
  color: #667eea;
  margin-top: 8px;
  padding: 8px;
  background: #F8F9FF;
  border-radius: 8px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
}

.btn {
  font-family: 'Manrope', sans-serif;
  font-size: 14px;
  font-weight: 600;
  padding: 12px 24px;
  border-radius: 10px;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary {
  background: #F0F0F0;
  color: #1B1B1B;
}

.btn-secondary:hover {
  background: #E0E0E0;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.form-text {
  font-size: 12px;
  color: #B2AEAB;
  margin-top: 8px;
}

/* Анимация загрузки */
.spinner-border {
  vertical-align: middle;
}

/* Адаптивность */
@media (max-width: 768px) {
  .add-book-form {
    padding: 20px;
  }
  
  .form-title {
    font-size: 20px;
  }
  
  .row {
    margin: 0 -8px;
  }
  
  .col-md-6, .col-md-4, .col-md-8 {
    padding: 0 8px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn {
    width: 100%;
  }
}
</style>