import { JwtPayload } from "jwt-decode";

export const BASE_URL = 'http://localhost:9090/api/v1';

export interface JWTCustomPayload extends JwtPayload {
    isAdmin: boolean;
    email: string;
    name: string;
}

export interface UserLoginType {
    username: string;
    password: string;
}
export interface UserRegisterType extends UserLoginType {
    name: string;
    email: string;
    isAdmin: boolean;
}

export interface UserType extends Omit<UserRegisterType, 'password'> {
    id: number;
}

export interface QuestionPostType {
    question: string;
    topic: string;
    images: string[];
}

export interface QuestionType extends QuestionPostType {
    id: number;
    postedBy: string;
	postedAt: Date;
	approvedBy: string;
	isApproved: boolean;
}

export interface AnswerPostType {
    answer: string;
    images: string[];
}

export interface AnswerType extends AnswerPostType {
    id: number;
    postedBy: string;
	postedAt: Date;
	approvedBy: string;
	isApproved: boolean;
    question: QuestionType;
}

export const QUESTIONS_TOPICS = [
    'Actors',
    'Algebra',
    'Applications',
    'Arts',
    'Books',
    'Climate Change',
    'Comedy / Humour',
    'Commerce',
    'Computer Science',
    'Current Affairs',
    'Electronics',
    'Engineering',
    'Food',
    'Games',
    'General',
    'General Knowledge',
    'History',
    'Hobbies',
    'Humanity',
    'Information Technology',
    'Internet',
    'Mathematics',
    'Movies',
    'Music',
    'Pets',
    'Philosophy',
    'Programming',
    'Random',
    'Restaurants',
    'Science',
    'Television',
    'Travel',
    'Weather',
];
