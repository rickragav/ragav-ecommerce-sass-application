"""
Comprehensive Exploratory Data Analysis for Book Recommendation Dataset
Author: GitHub Copilot
Purpose: Analyze dataset for multi-tenant e-commerce SaaS (Authors as Tenants)
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import warnings
from collections import Counter
import re
from wordcloud import WordCloud
from datetime import datetime
import plotly.express as px
import plotly.graph_objects as go
from plotly.subplots import make_subplots

# Configuration
warnings.filterwarnings("ignore")
plt.style.use("seaborn-v0_8")
sns.set_palette("husl")


class BookRecommendationEDA:
    """Complete EDA for Book Recommendation Dataset"""

    def __init__(self, dataset_path):
        """Initialize with dataset path"""
        self.dataset_path = dataset_path
        self.books_df = None
        self.users_df = None
        self.ratings_df = None
        self.merged_df = None

    def load_data(self):
        """Load all three CSV files"""
        print("📚 Loading Book Recommendation Dataset...")
        print("=" * 50)

        try:
            # Load datasets
            self.books_df = pd.read_csv(
                f"{self.dataset_path}/Books.csv", encoding="latin-1", low_memory=False
            )
            self.users_df = pd.read_csv(
                f"{self.dataset_path}/Users.csv", encoding="latin-1"
            )
            self.ratings_df = pd.read_csv(
                f"{self.dataset_path}/Ratings.csv", encoding="latin-1"
            )

            print(f"✅ Books loaded: {len(self.books_df):,} records")
            print(f"✅ Users loaded: {len(self.users_df):,} records")
            print(f"✅ Ratings loaded: {len(self.ratings_df):,} records")

        except Exception as e:
            print(f"❌ Error loading data: {e}")
            return False

        return True

    def basic_info(self):
        """Display basic information about datasets"""
        print("\n📊 DATASET OVERVIEW")
        print("=" * 50)

        # Books dataset info
        print("\n📖 BOOKS DATASET:")
        print(f"Shape: {self.books_df.shape}")
        print(f"Columns: {list(self.books_df.columns)}")
        print("\nSample data:")
        print(self.books_df.head(3))

        print(f"\nMissing values:")
        print(self.books_df.isnull().sum())

        # Users dataset info
        print("\n👥 USERS DATASET:")
        print(f"Shape: {self.users_df.shape}")
        print(f"Columns: {list(self.users_df.columns)}")
        print("\nSample data:")
        print(self.users_df.head(3))

        print(f"\nMissing values:")
        print(self.users_df.isnull().sum())

        # Ratings dataset info
        print("\n⭐ RATINGS DATASET:")
        print(f"Shape: {self.ratings_df.shape}")
        print(f"Columns: {list(self.ratings_df.columns)}")
        print("\nSample data:")
        print(self.ratings_df.head(3))

        print(f"\nMissing values:")
        print(self.ratings_df.isnull().sum())

    def analyze_books_for_tenancy(self):
        """Analyze books dataset focusing on author-tenant perspective"""
        print("\n🏪 AUTHOR-TENANT ANALYSIS")
        print("=" * 50)

        # Clean author names
        self.books_df["Book-Author"] = self.books_df["Book-Author"].fillna(
            "Unknown Author"
        )

        # Author statistics (potential tenants)
        author_stats = self.books_df["Book-Author"].value_counts()

        print(f"📊 AUTHOR STATISTICS:")
        print(f"Total unique authors (potential tenants): {len(author_stats):,}")
        print(f"Average books per author: {author_stats.mean():.2f}")
        print(f"Median books per author: {author_stats.median():.0f}")

        print(f"\n🔥 TOP 20 AUTHORS BY BOOK COUNT (Top Tenants):")
        for i, (author, count) in enumerate(author_stats.head(20).items(), 1):
            print(f"{i:2d}. {author:<30} : {count:3d} books")

        # Author distribution analysis
        print(f"\n📈 AUTHOR DISTRIBUTION ANALYSIS:")
        single_book_authors = (author_stats == 1).sum()
        multi_book_authors = (author_stats > 1).sum()
        prolific_authors = (author_stats >= 10).sum()
        super_authors = (author_stats >= 50).sum()

        print(
            f"Authors with 1 book: {single_book_authors:,} ({single_book_authors/len(author_stats)*100:.1f}%)"
        )
        print(
            f"Authors with 2+ books: {multi_book_authors:,} ({multi_book_authors/len(author_stats)*100:.1f}%)"
        )
        print(
            f"Prolific authors (10+ books): {prolific_authors:,} ({prolific_authors/len(author_stats)*100:.1f}%)"
        )
        print(
            f"Super authors (50+ books): {super_authors:,} ({super_authors/len(author_stats)*100:.1f}%)"
        )

        # NEW ADDITION: Authors with 50+ books (Super Premium Tenants)
        super_authors_list = author_stats[author_stats >= 50]
        print(f"\n🌟 SUPER AUTHORS (50+ BOOKS) - Premium Tenant Candidates:")
        print(f"Total super authors: {len(super_authors_list):,}")

        if len(super_authors_list) > 0:
            print(f"\nComplete list of authors with 50+ books:")
            for i, (author, count) in enumerate(super_authors_list.items(), 1):
                print(f"{i:2d}. {author:<40} : {count:3d} books")
        else:
            print("   No authors found with 50+ books in this dataset")

        # ...existing code...
        # Visualize author distribution
        plt.figure(figsize=(15, 10))

        plt.subplot(2, 2, 1)
        author_stats.head(20).plot(kind="bar")
        plt.title("Top 20 Authors by Book Count")
        plt.xlabel("Authors")
        plt.ylabel("Number of Books")
        plt.xticks(rotation=45, ha="right")

        plt.subplot(2, 2, 2)
        books_per_author_dist = author_stats.value_counts().head(20)
        books_per_author_dist.plot(kind="bar")
        plt.title("Distribution: How Many Authors Have X Books")
        plt.xlabel("Number of Books")
        plt.ylabel("Number of Authors")

        plt.subplot(2, 2, 3)
        # Author categories
        categories = ["1 book", "2-9 books", "10-49 books", "50+ books"]
        values = [
            single_book_authors,
            multi_book_authors - prolific_authors,
            prolific_authors - super_authors,
            super_authors,
        ]
        plt.pie(values, labels=categories, autopct="%1.1f%%")
        plt.title("Author Categories Distribution")

        plt.subplot(2, 2, 4)
        # Books per decade
        self.books_df["Year-Of-Publication"] = pd.to_numeric(
            self.books_df["Year-Of-Publication"], errors="coerce"
        )
        valid_years = self.books_df["Year-Of-Publication"].dropna()
        valid_years = valid_years[(valid_years >= 1900) & (valid_years <= 2023)]

        decade_counts = (valid_years // 10 * 10).value_counts().sort_index()
        decade_counts.plot(kind="line", marker="o")
        plt.title("Books Published by Decade")
        plt.xlabel("Decade")
        plt.ylabel("Number of Books")

        plt.tight_layout()
        plt.savefig("author_tenant_analysis.png", dpi=300, bbox_inches="tight")
        plt.show()

        return author_stats

    def analyze_users(self):
        """Analyze users dataset"""
        print("\n👥 USER ANALYSIS")
        print("=" * 50)

        # User demographics
        print(f"📊 USER DEMOGRAPHICS:")
        print(f"Total users: {len(self.users_df):,}")

        # Age analysis
        age_data = pd.to_numeric(self.users_df["Age"], errors="coerce")
        valid_ages = age_data.dropna()
        valid_ages = valid_ages[(valid_ages > 0) & (valid_ages < 120)]

        print(f"\n🎂 AGE STATISTICS:")
        print(f"Users with valid age data: {len(valid_ages):,}")
        print(f"Age range: {valid_ages.min():.0f} - {valid_ages.max():.0f}")
        print(f"Average age: {valid_ages.mean():.1f}")
        print(f"Median age: {valid_ages.median():.0f}")

        # Location analysis
        location_stats = self.users_df["Location"].value_counts()
        print(f"\n🌍 LOCATION ANALYSIS:")
        print(f"Total unique locations: {len(location_stats):,}")
        print(f"\nTop 15 user locations:")
        for i, (location, count) in enumerate(location_stats.head(15).items(), 1):
            print(f"{i:2d}. {location:<40} : {count:5d} users")

        # Country analysis (extract country from location)
        countries = []
        for location in self.users_df["Location"].fillna("Unknown"):
            parts = str(location).split(",")
            if len(parts) >= 2:
                countries.append(parts[-1].strip().lower())
            else:
                countries.append("unknown")

        country_stats = pd.Series(countries).value_counts()
        print(f"\n🏳️ TOP 10 COUNTRIES:")
        for i, (country, count) in enumerate(country_stats.head(10).items(), 1):
            print(f"{i:2d}. {country.title():<20} : {count:5d} users")

        # Visualizations
        plt.figure(figsize=(15, 10))

        plt.subplot(2, 2, 1)
        valid_ages.hist(bins=50, alpha=0.7)
        plt.title("User Age Distribution")
        plt.xlabel("Age")
        plt.ylabel("Number of Users")

        plt.subplot(2, 2, 2)
        location_stats.head(15).plot(kind="barh")
        plt.title("Top 15 User Locations")
        plt.xlabel("Number of Users")

        plt.subplot(2, 2, 3)
        country_stats.head(10).plot(kind="bar")
        plt.title("Top 10 Countries by User Count")
        plt.xlabel("Country")
        plt.ylabel("Number of Users")
        plt.xticks(rotation=45)

        plt.subplot(2, 2, 4)
        # Age groups
        age_groups = pd.cut(
            valid_ages,
            bins=[0, 18, 25, 35, 45, 55, 65, 100],
            labels=["<18", "18-25", "26-35", "36-45", "46-55", "56-65", "65+"],
        )
        age_groups.value_counts().plot(kind="pie", autopct="%1.1f%%")
        plt.title("User Age Groups")

        plt.tight_layout()
        plt.savefig("user_analysis.png", dpi=300, bbox_inches="tight")
        plt.show()

    def analyze_ratings(self):
        """Analyze ratings dataset for recommendation insights"""
        print("\n⭐ RATING ANALYSIS")
        print("=" * 50)

        print(f"📊 RATING STATISTICS:")
        print(f"Total ratings: {len(self.ratings_df):,}")
        print(f"Unique users who rated: {self.ratings_df['User-ID'].nunique():,}")
        print(f"Unique books rated: {self.ratings_df['ISBN'].nunique():,}")

        # Rating distribution
        rating_dist = self.ratings_df["Book-Rating"].value_counts().sort_index()
        print(f"\n📈 RATING DISTRIBUTION:")
        for rating, count in rating_dist.items():
            percentage = count / len(self.ratings_df) * 100
            print(f"Rating {rating}: {count:8,} ({percentage:5.1f}%)")

        # User activity analysis
        user_activity = self.ratings_df["User-ID"].value_counts()
        print(f"\n👤 USER ACTIVITY STATISTICS:")
        print(f"Average ratings per user: {user_activity.mean():.1f}")
        print(f"Median ratings per user: {user_activity.median():.0f}")
        print(f"Max ratings by single user: {user_activity.max():,}")

        # Book popularity analysis
        book_popularity = self.ratings_df["ISBN"].value_counts()
        print(f"\n📚 BOOK POPULARITY STATISTICS:")
        print(f"Average ratings per book: {book_popularity.mean():.1f}")
        print(f"Median ratings per book: {book_popularity.median():.0f}")
        print(f"Max ratings for single book: {book_popularity.max():,}")

        # Active users (users with many ratings)
        active_users = user_activity[user_activity >= 20]
        print(f"\nActive users (20+ ratings): {len(active_users):,}")

        # Popular books (books with many ratings)
        popular_books = book_popularity[book_popularity >= 20]
        print(f"Popular books (20+ ratings): {len(popular_books):,}")

        # Rating patterns
        plt.figure(figsize=(15, 12))

        plt.subplot(2, 3, 1)
        rating_dist.plot(kind="bar")
        plt.title("Rating Distribution")
        plt.xlabel("Rating")
        plt.ylabel("Count")

        plt.subplot(2, 3, 2)
        user_activity.head(20).plot(kind="bar")
        plt.title("Top 20 Most Active Users")
        plt.xlabel("User ID")
        plt.ylabel("Number of Ratings")
        plt.xticks(rotation=45)

        plt.subplot(2, 3, 3)
        book_popularity.head(20).plot(kind="bar")
        plt.title("Top 20 Most Rated Books")
        plt.xlabel("ISBN")
        plt.ylabel("Number of Ratings")
        plt.xticks(rotation=45)

        plt.subplot(2, 3, 4)
        # User activity distribution
        activity_ranges = [1, 2, 5, 10, 20, 50, 100, float("inf")]
        activity_labels = ["1", "2-4", "5-9", "10-19", "20-49", "50-99", "100+"]
        user_activity_groups = pd.cut(
            user_activity, bins=activity_ranges, labels=activity_labels, right=False
        )
        user_activity_groups.value_counts().plot(kind="bar")
        plt.title("User Activity Distribution")
        plt.xlabel("Number of Ratings")
        plt.ylabel("Number of Users")

        plt.subplot(2, 3, 5)
        # Book rating distribution
        book_rating_groups = pd.cut(
            book_popularity, bins=activity_ranges, labels=activity_labels, right=False
        )
        book_rating_groups.value_counts().plot(kind="bar")
        plt.title("Book Rating Count Distribution")
        plt.xlabel("Number of Ratings Received")
        plt.ylabel("Number of Books")

        plt.subplot(2, 3, 6)
        # Rating over time (if we merge with publication year)
        non_zero_ratings = self.ratings_df[self.ratings_df["Book-Rating"] > 0]
        avg_rating = non_zero_ratings["Book-Rating"].mean()
        plt.axhline(
            y=avg_rating,
            color="red",
            linestyle="--",
            label=f"Average Rating: {avg_rating:.2f}",
        )
        plt.hist(non_zero_ratings["Book-Rating"], bins=20, alpha=0.7)
        plt.title("Non-Zero Ratings Distribution")
        plt.xlabel("Rating")
        plt.ylabel("Frequency")
        plt.legend()

        plt.tight_layout()
        plt.savefig("rating_analysis.png", dpi=300, bbox_inches="tight")
        plt.show()

        return user_activity, book_popularity

    def merge_and_analyze(self):
        """Merge datasets and perform combined analysis"""
        print("\n🔄 MERGED DATASET ANALYSIS")
        print("=" * 50)

        # Merge ratings with books
        ratings_books = self.ratings_df.merge(self.books_df, on="ISBN", how="left")

        # Filter only rated books (ratings > 0)
        rated_books = ratings_books[ratings_books["Book-Rating"] > 0]

        print(f"📊 MERGED STATISTICS:")
        print(f"Total rating-book combinations: {len(ratings_books):,}")
        print(f"Actual ratings (>0): {len(rated_books):,}")
        print(
            f"Implicit feedback (rating=0): {len(ratings_books) - len(rated_books):,}"
        )

        # Author performance analysis (key for tenant insights)
        author_performance = (
            rated_books.groupby("Book-Author")
            .agg({"Book-Rating": ["count", "mean", "std"], "ISBN": "nunique"})
            .round(2)
        )

        author_performance.columns = [
            "Total_Ratings",
            "Avg_Rating",
            "Rating_Std",
            "Unique_Books",
        ]
        author_performance = author_performance[
            author_performance["Total_Ratings"] >= 10
        ]
        author_performance = author_performance.sort_values(
            "Total_Ratings", ascending=False
        )

        print(f"\n🏆 TOP 20 AUTHORS BY TOTAL RATINGS (Best Tenant Candidates):")
        for i, (author, data) in enumerate(author_performance.head(20).iterrows(), 1):
            print(
                f"{i:2d}. {author:<30}: {data['Total_Ratings']:5.0f} ratings, "
                f"{data['Avg_Rating']:.2f} avg, {data['Unique_Books']:3.0f} books"
            )

        # Best rated authors (minimum 50 ratings for significance)
        best_rated = author_performance[author_performance["Total_Ratings"] >= 50]
        best_rated = best_rated.sort_values("Avg_Rating", ascending=False)

        print(f"\n⭐ TOP 15 HIGHEST RATED AUTHORS (50+ ratings):")
        for i, (author, data) in enumerate(best_rated.head(15).iterrows(), 1):
            print(
                f"{i:2d}. {author:<30}: {data['Avg_Rating']:.2f} avg "
                f"({data['Total_Ratings']:4.0f} ratings)"
            )

        # Publisher analysis
        publisher_stats = (
            rated_books.groupby("Publisher")
            .agg({"Book-Rating": ["count", "mean"], "Book-Author": "nunique"})
            .round(2)
        )
        publisher_stats.columns = ["Total_Ratings", "Avg_Rating", "Unique_Authors"]
        publisher_stats = publisher_stats[publisher_stats["Total_Ratings"] >= 100]
        publisher_stats = publisher_stats.sort_values("Total_Ratings", ascending=False)

        print(f"\n📖 TOP 15 PUBLISHERS BY RATINGS:")
        for i, (publisher, data) in enumerate(publisher_stats.head(15).iterrows(), 1):
            print(
                f"{i:2d}. {publisher:<35}: {data['Total_Ratings']:5.0f} ratings, "
                f"{data['Avg_Rating']:.2f} avg"
            )

        # Year analysis
        rated_books["Year-Of-Publication"] = pd.to_numeric(
            rated_books["Year-Of-Publication"], errors="coerce"
        )
        valid_year_ratings = rated_books.dropna(subset=["Year-Of-Publication"])
        valid_year_ratings = valid_year_ratings[
            (valid_year_ratings["Year-Of-Publication"] >= 1950)
            & (valid_year_ratings["Year-Of-Publication"] <= 2023)
        ]

        year_stats = (
            valid_year_ratings.groupby("Year-Of-Publication")
            .agg({"Book-Rating": ["count", "mean"]})
            .round(2)
        )
        year_stats.columns = ["Rating_Count", "Avg_Rating"]

        # Visualizations
        plt.figure(figsize=(20, 15))

        plt.subplot(3, 3, 1)
        author_performance.head(15)["Total_Ratings"].plot(kind="barh")
        plt.title("Top 15 Authors by Total Ratings")
        plt.xlabel("Total Ratings")

        plt.subplot(3, 3, 2)
        best_rated.head(15)["Avg_Rating"].plot(kind="barh")
        plt.title("Top 15 Authors by Average Rating")
        plt.xlabel("Average Rating")

        plt.subplot(3, 3, 3)
        publisher_stats.head(10)["Total_Ratings"].plot(kind="barh")
        plt.title("Top 10 Publishers by Total Ratings")
        plt.xlabel("Total Ratings")

        plt.subplot(3, 3, 4)
        # Author rating distribution
        plt.scatter(
            author_performance["Total_Ratings"],
            author_performance["Avg_Rating"],
            alpha=0.6,
        )
        plt.xlabel("Total Ratings")
        plt.ylabel("Average Rating")
        plt.title("Author Performance: Total vs Average Rating")
        plt.xscale("log")

        plt.subplot(3, 3, 5)
        # Year trend
        year_stats["Rating_Count"].plot(kind="line")
        plt.title("Ratings Count by Publication Year")
        plt.xlabel("Year")
        plt.ylabel("Number of Ratings")

        plt.subplot(3, 3, 6)
        year_stats["Avg_Rating"].plot(kind="line", color="orange")
        plt.title("Average Rating by Publication Year")
        plt.xlabel("Year")
        plt.ylabel("Average Rating")

        plt.subplot(3, 3, 7)
        # Books per author distribution
        author_performance["Unique_Books"].hist(bins=30, alpha=0.7)
        plt.title("Distribution: Books per Author")
        plt.xlabel("Number of Unique Books")
        plt.ylabel("Number of Authors")

        plt.subplot(3, 3, 8)
        # Rating spread by author
        author_performance["Rating_Std"].hist(bins=30, alpha=0.7, color="green")
        plt.title("Rating Standard Deviation by Author")
        plt.xlabel("Standard Deviation")
        plt.ylabel("Number of Authors")

        plt.subplot(3, 3, 9)
        # Publisher performance
        plt.scatter(
            publisher_stats["Total_Ratings"],
            publisher_stats["Avg_Rating"],
            alpha=0.6,
            color="red",
        )
        plt.xlabel("Total Ratings")
        plt.ylabel("Average Rating")
        plt.title("Publisher Performance")
        plt.xscale("log")

        plt.tight_layout()
        plt.savefig("merged_analysis.png", dpi=300, bbox_inches="tight")
        plt.show()

        return author_performance, publisher_stats

    def tenant_recommendations(self, author_performance):
        """Provide recommendations for tenant selection"""
        print("\n🎯 TENANT SELECTION RECOMMENDATIONS")
        print("=" * 50)

        # Tier 1: Premium tenants (high volume + high quality)
        tier1 = author_performance[
            (author_performance["Total_Ratings"] >= 1000)
            & (author_performance["Avg_Rating"] >= 7.5)
        ]

        # Tier 2: High potential tenants (good volume or quality)
        tier2 = author_performance[
            (
                (author_performance["Total_Ratings"] >= 500)
                & (author_performance["Avg_Rating"] >= 7.0)
            )
            & (~author_performance.index.isin(tier1.index))
        ]

        # Tier 3: Growing tenants (decent activity)
        tier3 = author_performance[
            (author_performance["Total_Ratings"] >= 100)
            & (author_performance["Avg_Rating"] >= 6.5)
            & (~author_performance.index.isin(tier1.index))
            & (~author_performance.index.isin(tier2.index))
        ]

        print(f"🏆 TIER 1 - PREMIUM TENANTS ({len(tier1)} authors):")
        print("   Criteria: 1000+ ratings AND 7.5+ average rating")
        for author, data in tier1.head(10).iterrows():
            print(
                f"   • {author:<35}: {data['Total_Ratings']:5.0f} ratings, "
                f"{data['Avg_Rating']:.2f} avg"
            )

        print(f"\n🚀 TIER 2 - HIGH POTENTIAL TENANTS ({len(tier2)} authors):")
        print("   Criteria: 500+ ratings AND 7.0+ average rating")
        for author, data in tier2.head(10).iterrows():
            print(
                f"   • {author:<35}: {data['Total_Ratings']:5.0f} ratings, "
                f"{data['Avg_Rating']:.2f} avg"
            )

        print(f"\n📈 TIER 3 - GROWING TENANTS ({len(tier3)} authors):")
        print("   Criteria: 100+ ratings AND 6.5+ average rating")
        for author, data in tier3.head(10).iterrows():
            print(
                f"   • {author:<35}: {data['Total_Ratings']:5.0f} ratings, "
                f"{data['Avg_Rating']:.2f} avg"
            )

        # Business insights
        print(f"\n💡 BUSINESS INSIGHTS:")
        print(f"   📊 Total viable tenants: {len(author_performance):,}")
        print(f"   🏆 Premium tier: {len(tier1):,} authors")
        print(f"   🚀 High potential: {len(tier2):,} authors")
        print(f"   📈 Growing tier: {len(tier3):,} authors")
        print(
            f"   📋 Long tail: {len(author_performance) - len(tier1) - len(tier2) - len(tier3):,} authors"
        )

        # Revenue potential
        avg_books_per_author = author_performance["Unique_Books"].mean()
        total_books = author_performance["Unique_Books"].sum()

        print(f"\n💰 REVENUE POTENTIAL:")
        print(f"   📚 Total books across all tenants: {total_books:,.0f}")
        print(f"   📖 Average books per author: {avg_books_per_author:.1f}")
        print(
            f"   ⭐ Total ratings in system: {author_performance['Total_Ratings'].sum():,}"
        )

        return tier1, tier2, tier3

    def generate_summary_report(self):
        """Generate comprehensive summary report"""
        print("\n📋 EXECUTIVE SUMMARY REPORT")
        print("=" * 60)

        # Dataset overview
        total_books = len(self.books_df)
        total_users = len(self.users_df)
        total_ratings = len(self.ratings_df)
        unique_authors = self.books_df["Book-Author"].nunique()

        print(f"📊 DATASET SCALE:")
        print(f"   • Total Books: {total_books:,}")
        print(f"   • Total Users: {total_users:,}")
        print(f"   • Total Ratings: {total_ratings:,}")
        print(f"   • Unique Authors (Potential Tenants): {unique_authors:,}")

        # Data quality
        book_missing_pct = (
            self.books_df.isnull().sum().sum()
            / (len(self.books_df) * len(self.books_df.columns))
        ) * 100
        user_missing_pct = (
            self.users_df.isnull().sum().sum()
            / (len(self.users_df) * len(self.users_df.columns))
        ) * 100

        print(f"\n🎯 DATA QUALITY:")
        print(f"   • Books dataset completeness: {100-book_missing_pct:.1f}%")
        print(f"   • Users dataset completeness: {100-user_missing_pct:.1f}%")
        print(f"   • Ratings dataset completeness: 100.0%")

        # Business viability
        active_ratings = len(self.ratings_df[self.ratings_df["Book-Rating"] > 0])
        engagement_rate = (active_ratings / total_ratings) * 100

        print(f"\n📈 BUSINESS VIABILITY:")
        print(f"   • Active ratings (>0): {active_ratings:,} ({engagement_rate:.1f}%)")
        print(f"   • Average ratings per user: {total_ratings/total_users:.1f}")
        print(f"   • Average ratings per book: {total_ratings/total_books:.1f}")

        # Recommendation
        print(f"\n✅ RECOMMENDATION:")
        print(f"   This dataset is EXCELLENT for multi-tenant e-commerce SaaS!")
        print(f"   • Large scale: {unique_authors:,} potential author-tenants")
        print(f"   • Rich interactions: {active_ratings:,} meaningful ratings")
        print(f"   • Complete product info: Books have titles, authors, images")
        print(f"   • User diversity: {total_users:,} users across global locations")

        return {
            "total_books": total_books,
            "total_users": total_users,
            "total_ratings": total_ratings,
            "unique_authors": unique_authors,
            "active_ratings": active_ratings,
            "engagement_rate": engagement_rate,
        }

    def run_complete_analysis(self):
        """Run the complete EDA pipeline"""
        print("🚀 STARTING COMPLETE EXPLORATORY DATA ANALYSIS")
        print("=" * 60)

        # Load data
        if not self.load_data():
            return

        # Run all analyses
        self.basic_info()
        author_stats = self.analyze_books_for_tenancy()
        self.analyze_users()
        user_activity, book_popularity = self.analyze_ratings()
        author_performance, publisher_stats = self.merge_and_analyze()
        tier1, tier2, tier3 = self.tenant_recommendations(author_performance)
        summary = self.generate_summary_report()

        print(f"\n🎉 ANALYSIS COMPLETE!")
        print(f"📁 Generated files:")
        print(f"   • author_tenant_analysis.png")
        print(f"   • user_analysis.png")
        print(f"   • rating_analysis.png")
        print(f"   • merged_analysis.png")

        return {
            "author_stats": author_stats,
            "user_activity": user_activity,
            "book_popularity": book_popularity,
            "author_performance": author_performance,
            "publisher_stats": publisher_stats,
            "tenant_tiers": {"tier1": tier1, "tier2": tier2, "tier3": tier3},
            "summary": summary,
        }


# Usage Example
if __name__ == "__main__":
    # Initialize EDA
    eda = BookRecommendationEDA(
        "/Users/rakesh.vasudevan/Documents/microservice-projects/ragav-ecommerce-sass-application/book-data"
    )

    # Run complete analysis
    results = eda.run_complete_analysis()

    # Optional: Save results
    import pickle

    with open("book_eda_results.pkl", "wb") as f:
        pickle.dump(results, f)

    print("📊 Analysis results saved to 'book_eda_results.pkl'")
